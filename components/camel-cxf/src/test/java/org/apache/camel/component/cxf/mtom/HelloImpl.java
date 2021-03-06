begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.mtom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|mtom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

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
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
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
name|cxf
operator|.
name|mtom_feature
operator|.
name|Hello
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  * Hello Test Impl class  */
end_comment

begin_class
DECL|class|HelloImpl
specifier|public
class|class
name|HelloImpl
implements|implements
name|Hello
block|{
DECL|method|detail (Holder<byte[]> photo, Holder<Image> image)
specifier|public
name|void
name|detail
parameter_list|(
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|photo
parameter_list|,
name|Holder
argument_list|<
name|Image
argument_list|>
name|image
parameter_list|)
block|{
name|MtomTestHelper
operator|.
name|assertEquals
argument_list|(
name|MtomTestHelper
operator|.
name|REQ_PHOTO_DATA
argument_list|,
name|photo
operator|.
name|value
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|image
operator|.
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|image
operator|.
name|value
operator|instanceof
name|BufferedImage
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|41
argument_list|,
operator|(
operator|(
name|BufferedImage
operator|)
name|image
operator|.
name|value
operator|)
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|39
argument_list|,
operator|(
operator|(
name|BufferedImage
operator|)
name|image
operator|.
name|value
operator|)
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|image
operator|.
name|value
operator|=
name|ImageIO
operator|.
name|read
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/Splash.jpg"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|photo
operator|.
name|value
operator|=
name|MtomTestHelper
operator|.
name|RESP_PHOTO_DATA
expr_stmt|;
block|}
DECL|method|echoData (Holder<byte[]> data)
specifier|public
name|void
name|echoData
parameter_list|(
name|Holder
argument_list|<
name|byte
index|[]
argument_list|>
name|data
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

