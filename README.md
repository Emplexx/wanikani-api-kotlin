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
I'll finish writing this later
