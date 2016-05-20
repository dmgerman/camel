begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
package|;
end_package

begin_comment
comment|/**  * A collection of supported media type for outgoing messages (produced).  * It is meant to be put in the message headers using key {@link TelegramConstants#TELEGRAM_MEDIA_TYPE}.  */
end_comment

begin_enum
DECL|enum|TelegramMediaType
specifier|public
enum|enum
name|TelegramMediaType
block|{
DECL|enumConstant|TEXT
DECL|enumConstant|PHOTO_PNG
DECL|enumConstant|PHOTO_JPG
DECL|enumConstant|AUDIO
DECL|enumConstant|VIDEO
name|TEXT
argument_list|(
literal|"txt"
argument_list|)
block|,
name|PHOTO_PNG
argument_list|(
literal|"png"
argument_list|)
block|,
name|PHOTO_JPG
argument_list|(
literal|"jpg"
argument_list|)
block|,
name|AUDIO
argument_list|(
literal|"mp3"
argument_list|)
block|,
name|VIDEO
argument_list|(
literal|"mp4"
argument_list|)
block|;
DECL|field|fileExtension
specifier|private
name|String
name|fileExtension
decl_stmt|;
DECL|method|TelegramMediaType (String fileExtension)
name|TelegramMediaType
parameter_list|(
name|String
name|fileExtension
parameter_list|)
block|{
name|this
operator|.
name|fileExtension
operator|=
name|fileExtension
expr_stmt|;
block|}
DECL|method|getFileExtension ()
specifier|public
name|String
name|getFileExtension
parameter_list|()
block|{
return|return
name|fileExtension
return|;
block|}
block|}
end_enum

end_unit

