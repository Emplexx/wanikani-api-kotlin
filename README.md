# I Debated Calling This KaniKani

Kotlin wrapper for WaniKani V2 API 

Maybe I'll publish to a repository later

## Basic Usage

Create the client: 
```kotlin
val client = Wanikani("your access token here")
```
Create a `Request`:
```kotlin 
val request: Request<Report<Summary>> = client.getSummary()
```
Execute a `Request`:
```kotlin 
val response: Response<Report<Summary>> = request.execute()
```
`.execute()` executes the request and returns a custom `Response` type. It is possible to make it return a different type if you prefer, please see the Advanced usage section.

Note: The library refers to [collections](https://docs.api.wanikani.com/20170710/#response-structure) as `ResourceSet` to avoid conflict with Kotlin's `kotlin.collections.Collection`

## Features   
### Typed conditional requests  
[Conditional requests](https://docs.api.wanikani.com/20170710/#conditional-requests) may return an empty body. The library encodes this in the type system using a nullable type.  
  
```kotlin  
val timestamp: Timestamp = // ... 

//                  vvv note the type parameter of the `Request`  
val nonConditional: Request<ResourceSet<Subject>> = client.getSubjects()  
val conditional:    Request<ResourceSet<Subject>?> = client
	.getSubjects()
	// If-Modified-Since header
	.ifModifiedSince(Clock.System.now())  
```

### Pagination  
WaniKani returns a `pages` object for collection requests. It includes `next_url` and `previous_url` attributes (`null` or String), to get the next and previous pages respectively.
The client includes `getNextPage` and `getPreviousPage` functions that let you use the `ResourceSet<T>` data class, or the URL directly.

```kotlin
val page1: ResourceSet<Subject> = client.getSubjects().execute().getOrThrow()

// get*Page(ResourceSet<T>) returns null if the URL of that page is null
val page2: ResourceSet<Subject> = client.getNextPage(page1)!!.execute().getOrThrow()

// alternatively, use the URL directly
val page3 = client.getNextPage(page2.pages.nextUrl!!).execute()
```

## Advanced Usage
### Configuring the client
The constructor function `Wanikani` has a second parameter `revision`, set to [`20170710`](https://docs.api.wanikani.com/20170710/#revisions-aka-versioning) by default. 
```kotlin
val client = Wanikani("token", revision = ...)
```

Alternatively, you can use the constructor that takes a `config` function:
- Set the `AuthProvider` to provide the token, which is a `fun interface` with a single `suspend` function that returns `String?`, for flexibility
- Configure the underlying Ktor `HttpClient`
```kotlin
val client = Wanikani {  
    authProvider = AuthProvider { magic() }  
    httpClient = HttpClient(OkHttp) {   
        install(Logging)  
    }  
    revision = 20501212 // WaniKani from the future
}
```

### Custom response type
`Request<A>` provides a `fold` function, that takes two parameters, a function `(HttpResponse, A) -> B` for successful responses, and `(HttpResponse) -> B` for unsuccessful ones. That is what `.execute()` uses under the hood:
```kotlin
suspend fun <A> Request<A>.execute(): Response<A> =  
    fold({ response, body ->  
       Response.Success(  
          body,  
          response.etag(),  
          response.lastModified()?.toInstant()?.toKotlinInstant()  
       )  
    }, { response ->  
       Response.Failure(  
          response.status.value,  
          runCatching<ErrorBody> { response.body() }.getOrNull()?.error  
       )  
    })
```
You could create an extension function on `Request<A>` to use a preferred response type. For example, if you wanted to use `Either` from [Arrow](https://github.com/arrow-kt/arrow), you could do the following:
```kotlin
suspend fun <A> Request<A>.toEither() = fold({ response, body ->  
    body.right()  
}, { response ->  
    response.status.left()  
})
```

