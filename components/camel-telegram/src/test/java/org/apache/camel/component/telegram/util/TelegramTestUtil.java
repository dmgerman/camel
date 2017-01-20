begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.util
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
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|IOUtils
import|;
end_import

begin_comment
comment|/**  * Utility functions for telegram tests.  */
end_comment

begin_class
DECL|class|TelegramTestUtil
specifier|public
specifier|final
class|class
name|TelegramTestUtil
block|{
DECL|method|TelegramTestUtil ()
specifier|private
name|TelegramTestUtil
parameter_list|()
block|{     }
comment|/**      * Creates a sample image.      *      * @param imageIOType the image-io code of the image type (eg. PNG, JPG)      * @return a sample image      * @throws IOException if anything goes wrong      */
DECL|method|createSampleImage (String imageIOType)
specifier|public
specifier|static
name|byte
index|[]
name|createSampleImage
parameter_list|(
name|String
name|imageIOType
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|img
decl_stmt|;
if|if
condition|(
name|imageIOType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"png"
argument_list|)
condition|)
block|{
name|img
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|TelegramTestUtil
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/attachments/sample.png"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|imageIOType
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"jpg"
argument_list|)
condition|)
block|{
name|img
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|TelegramTestUtil
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/attachments/sample.jpg"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown format "
operator|+
name|imageIOType
argument_list|)
throw|;
block|}
return|return
name|img
return|;
block|}
DECL|method|createSampleAudio ()
specifier|public
specifier|static
name|byte
index|[]
name|createSampleAudio
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|audio
init|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|TelegramTestUtil
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/attachments/sample.mp3"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|audio
return|;
block|}
DECL|method|createSampleVideo ()
specifier|public
specifier|static
name|byte
index|[]
name|createSampleVideo
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|video
init|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|TelegramTestUtil
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/attachments/sample.mp4"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|video
return|;
block|}
DECL|method|createSampleDocument ()
specifier|public
specifier|static
name|byte
index|[]
name|createSampleDocument
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|document
init|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|TelegramTestUtil
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/attachments/sample.png"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|document
return|;
block|}
block|}
end_class

end_unit

