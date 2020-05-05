package com.cshep4.premierpredictor.userscoreupdater.entity

import com.cshep4.premierpredictor.userscoreupdater.data.User
import org.bson.types.ObjectId
import org.bson.types.ObjectId.isValid
import java.time.LocalDateTime

data class UserEntity (
        var id: ObjectId = ObjectId(),
        var firstName: String= "",
        var surname: String = "",
        var email: String? = null,
        var password: String? = null,
        var predictedWinner: String = "",
        var score: Int = 0,
        var joined: LocalDateTime? = null,
        var admin: Boolean = false,
        var adFree: Boolean = false
){

    fun toDto(): User = User(
            id = this.id.toHexString(),
            firstName = this.firstName,
            surname = this.surname,
            email = this.email,
            password = this.password,
            predictedWinner = this.predictedWinner,
            score = this.score,
            joined = this.joined,
            admin = this.admin,
            adFree = this.adFree)

    companion object {
        fun fromDto(dto: User): UserEntity {
            if (!isValid(dto.id)) {
                throw IllegalArgumentException("Invalid id")
            }

            return UserEntity(
                    id = ObjectId(dto.id),
                    firstName = dto.firstName,
                    surname = dto.surname,
                    email = dto.email,
                    password = dto.password,
                    predictedWinner = dto.predictedWinner,
                    score = dto.score,
                    joined = dto.joined,
                    admin = dto.admin,
                    adFree = dto.adFree)
        }
    }
}