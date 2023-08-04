package com.vimal.rickandmorty.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vimal.rickandmorty.database.StringListConverter

@Entity(tableName = "characterDto_table")
data class CharacterDto(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String?,
    val gender: String,
    @Embedded(prefix = "origin_")
    val origin: OriginDto,
    @Embedded(prefix = "location_")
    val location: LocationDto,
    val image: String,
    @TypeConverters(StringListConverter::class)
    val episode: List<String>,
    val url: String,
    val created: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readParcelable(OriginDto::class.java.classLoader) ?: OriginDto("", ""),
        parcel.readParcelable(LocationDto::class.java.classLoader) ?: LocationDto("", ""),
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(status)
        parcel.writeString(species)
        parcel.writeString(type)
        parcel.writeString(gender)
        parcel.writeParcelable(origin, flags)
        parcel.writeParcelable(location, flags)
        parcel.writeString(image)
        parcel.writeStringList(episode)
        parcel.writeString(url)
        parcel.writeString(created)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CharacterDto> {
        override fun createFromParcel(parcel: Parcel): CharacterDto {
            return CharacterDto(parcel)
        }

        override fun newArray(size: Int): Array<CharacterDto?> {
            return arrayOfNulls(size)
        }
    }
}

data class OriginDto(
    val name: String,
    val url: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OriginDto> {
        override fun createFromParcel(parcel: Parcel): OriginDto {
            return OriginDto(parcel)
        }

        override fun newArray(size: Int): Array<OriginDto?> {
            return arrayOfNulls(size)
        }
    }
}

data class LocationDto(
    val name: String,
    val url: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationDto> {
        override fun createFromParcel(parcel: Parcel): LocationDto {
            return LocationDto(parcel)
        }

        override fun newArray(size: Int): Array<LocationDto?> {
            return arrayOfNulls(size)
        }
    }
}

data class CharacterListResponse(
    val info: PageInfo,
    val results: List<CharacterDto>
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
        parcel.writeInt(pages)
        parcel.writeString(next)
        parcel.writeString(prev)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PageInfo> {
        override fun createFromParcel(parcel: Parcel): PageInfo {
            return PageInfo(parcel)
        }

        override fun newArray(size: Int): Array<PageInfo?> {
            return arrayOfNulls(size)
        }
    }
}

data class CharacterLocation(
    val name: String,
    val url: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CharacterLocation> {
        override fun createFromParcel(parcel: Parcel): CharacterLocation {
            return CharacterLocation(parcel)
        }

        override fun newArray(size: Int): Array<CharacterLocation?> {
            return arrayOfNulls(size)
        }
    }
}
