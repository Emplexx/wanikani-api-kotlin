package moe.emi.wanikani

import io.kotest.matchers.shouldBe
import moe.emi.wanikani.type.calculateNewSrsStage
import kotlin.test.Test

class SrsStageLogicTest {
	
	@Test
	fun main() {
		calculateNewSrsStage(1, 0) shouldBe 2
		calculateNewSrsStage(2, 0) shouldBe 3
		calculateNewSrsStage(3, 0) shouldBe 4
		calculateNewSrsStage(4, 1) shouldBe 3
		calculateNewSrsStage(3, 0) shouldBe 4
		calculateNewSrsStage(4, 0) shouldBe 5
		calculateNewSrsStage(5, 0) shouldBe 6
		calculateNewSrsStage(6, 3) shouldBe 2
		
		// "The lowest stage is stage 1, so even if you answer something incorrectly a ton,
		// that’s as far down the scale as it will go."
		calculateNewSrsStage(6, 1234) shouldBe 1
	}
}