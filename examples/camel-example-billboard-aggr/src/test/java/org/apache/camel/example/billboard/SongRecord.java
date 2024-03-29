begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.billboard
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|billboard
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|CsvRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|annotation
operator|.
name|DataField
import|;
end_import

begin_class
annotation|@
name|CsvRecord
argument_list|(
name|separator
operator|=
literal|","
argument_list|,
name|crlf
operator|=
literal|"UNIX"
argument_list|)
DECL|class|SongRecord
specifier|public
class|class
name|SongRecord
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|)
DECL|field|rank
specifier|private
name|int
name|rank
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
argument_list|,
name|trim
operator|=
literal|true
argument_list|)
DECL|field|song
specifier|private
name|String
name|song
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|3
argument_list|,
name|trim
operator|=
literal|true
argument_list|)
DECL|field|artist
specifier|private
name|String
name|artist
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|4
argument_list|)
DECL|field|year
specifier|private
name|int
name|year
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|5
argument_list|,
name|trim
operator|=
literal|true
argument_list|)
DECL|field|lyrics
specifier|private
name|String
name|lyrics
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|6
argument_list|)
DECL|field|source
specifier|private
name|String
name|source
decl_stmt|;
DECL|method|SongRecord ()
specifier|public
name|SongRecord
parameter_list|()
block|{     }
DECL|method|SongRecord (int rank, String song, String artist, int year, String lyrics, String source)
specifier|public
name|SongRecord
parameter_list|(
name|int
name|rank
parameter_list|,
name|String
name|song
parameter_list|,
name|String
name|artist
parameter_list|,
name|int
name|year
parameter_list|,
name|String
name|lyrics
parameter_list|,
name|String
name|source
parameter_list|)
block|{
name|this
operator|.
name|rank
operator|=
name|rank
expr_stmt|;
name|this
operator|.
name|song
operator|=
name|song
expr_stmt|;
name|this
operator|.
name|artist
operator|=
name|artist
expr_stmt|;
name|this
operator|.
name|year
operator|=
name|year
expr_stmt|;
name|this
operator|.
name|lyrics
operator|=
name|lyrics
expr_stmt|;
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
DECL|method|getRank ()
specifier|public
name|int
name|getRank
parameter_list|()
block|{
return|return
name|this
operator|.
name|rank
return|;
block|}
DECL|method|setRank (int rank)
specifier|public
name|void
name|setRank
parameter_list|(
name|int
name|rank
parameter_list|)
block|{
name|this
operator|.
name|rank
operator|=
name|rank
expr_stmt|;
block|}
DECL|method|getSong ()
specifier|public
name|String
name|getSong
parameter_list|()
block|{
return|return
name|this
operator|.
name|song
return|;
block|}
DECL|method|setSong (String song)
specifier|public
name|void
name|setSong
parameter_list|(
name|String
name|song
parameter_list|)
block|{
name|this
operator|.
name|song
operator|=
name|song
expr_stmt|;
block|}
DECL|method|getArtist ()
specifier|public
name|String
name|getArtist
parameter_list|()
block|{
return|return
name|this
operator|.
name|artist
return|;
block|}
DECL|method|setArtist (String artist)
specifier|public
name|void
name|setArtist
parameter_list|(
name|String
name|artist
parameter_list|)
block|{
name|this
operator|.
name|artist
operator|=
name|artist
expr_stmt|;
block|}
DECL|method|getYear ()
specifier|public
name|int
name|getYear
parameter_list|()
block|{
return|return
name|this
operator|.
name|year
return|;
block|}
DECL|method|setYear (int year)
specifier|public
name|void
name|setYear
parameter_list|(
name|int
name|year
parameter_list|)
block|{
name|this
operator|.
name|year
operator|=
name|year
expr_stmt|;
block|}
DECL|method|getLyrics ()
specifier|public
name|String
name|getLyrics
parameter_list|()
block|{
return|return
name|this
operator|.
name|lyrics
return|;
block|}
DECL|method|setLyrics (String lyrics)
specifier|public
name|void
name|setLyrics
parameter_list|(
name|String
name|lyrics
parameter_list|)
block|{
name|this
operator|.
name|lyrics
operator|=
name|lyrics
expr_stmt|;
block|}
DECL|method|getSource ()
specifier|public
name|String
name|getSource
parameter_list|()
block|{
return|return
name|this
operator|.
name|source
return|;
block|}
DECL|method|setSource (String source)
specifier|public
name|void
name|setSource
parameter_list|(
name|String
name|source
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"{"
operator|+
literal|" rank='"
operator|+
name|getRank
argument_list|()
operator|+
literal|"'"
operator|+
literal|", song='"
operator|+
name|getSong
argument_list|()
operator|+
literal|"'"
operator|+
literal|", artist='"
operator|+
name|getArtist
argument_list|()
operator|+
literal|"'"
operator|+
literal|", year='"
operator|+
name|getYear
argument_list|()
operator|+
literal|"'"
operator|+
literal|", lyrics='"
operator|+
name|getLyrics
argument_list|()
operator|+
literal|"'"
operator|+
literal|", source='"
operator|+
name|getSource
argument_list|()
operator|+
literal|"'"
operator|+
literal|"}"
return|;
block|}
block|}
end_class

end_unit

