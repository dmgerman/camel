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
comment|/**  * Package local test helper  */
end_comment

begin_class
DECL|class|MtomTestHelper
specifier|public
specifier|final
class|class
name|MtomTestHelper
block|{
DECL|field|SERVICE_TYPES_NS
specifier|static
specifier|final
name|String
name|SERVICE_TYPES_NS
init|=
literal|"http://apache.org/camel/cxf/mtom_feature/types"
decl_stmt|;
DECL|field|XOP_NS
specifier|static
specifier|final
name|String
name|XOP_NS
init|=
literal|"http://www.w3.org/2004/08/xop/include"
decl_stmt|;
DECL|field|REQ_PHOTO_DATA
specifier|static
specifier|final
name|byte
index|[]
name|REQ_PHOTO_DATA
init|=
block|{
literal|1
block|,
literal|0
block|,
operator|-
literal|1
block|,
literal|34
block|,
literal|23
block|,
literal|3
block|,
literal|23
block|,
literal|55
block|,
literal|33
block|}
decl_stmt|;
DECL|field|RESP_PHOTO_DATA
specifier|static
specifier|final
name|byte
index|[]
name|RESP_PHOTO_DATA
init|=
block|{
literal|5
block|,
operator|-
literal|7
block|,
literal|23
block|,
literal|1
block|,
literal|0
block|,
operator|-
literal|1
block|,
literal|34
block|,
literal|23
block|,
literal|3
block|,
literal|23
block|,
literal|55
block|,
literal|33
block|,
literal|3
block|}
decl_stmt|;
DECL|field|REQ_PHOTO_CID
specifier|static
specifier|final
name|String
name|REQ_PHOTO_CID
init|=
literal|"e33b6792-6666-4837-b0d9-68c88c8cadcb-1@apache.org"
decl_stmt|;
DECL|field|REQ_IMAGE_CID
specifier|static
specifier|final
name|String
name|REQ_IMAGE_CID
init|=
literal|"e33b6792-6666-4837-b0d9-68c88c8cadcb-2@apache.org"
decl_stmt|;
DECL|field|REQ_MESSAGE
specifier|static
specifier|final
name|String
name|REQ_MESSAGE
init|=
literal|"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
operator|+
literal|"<Detail xmlns=\"http://apache.org/camel/cxf/mtom_feature/types\">"
operator|+
literal|"<photo><xop:Include xmlns:xop=\"http://www.w3.org/2004/08/xop/include\""
operator|+
literal|" href=\"cid:"
operator|+
name|REQ_PHOTO_CID
operator|+
literal|"\"/>"
operator|+
literal|"</photo><image><xop:Include xmlns:xop=\"http://www.w3.org/2004/08/xop/include\""
operator|+
literal|" href=\"cid:"
operator|+
name|REQ_IMAGE_CID
operator|+
literal|"\"/></image></Detail>"
decl_stmt|;
DECL|field|MTOM_DISABLED_REQ_MESSAGE
specifier|static
specifier|final
name|String
name|MTOM_DISABLED_REQ_MESSAGE
init|=
literal|"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
operator|+
literal|"<Detail xmlns=\"http://apache.org/camel/cxf/mtom_feature/types\">"
operator|+
literal|"<photo>cid:"
operator|+
name|REQ_PHOTO_CID
operator|+
literal|"</photo>"
operator|+
literal|"<image>cid:"
operator|+
name|REQ_IMAGE_CID
operator|+
literal|"</image></Detail>"
decl_stmt|;
DECL|field|RESP_PHOTO_CID
specifier|static
specifier|final
name|String
name|RESP_PHOTO_CID
init|=
literal|"4c7b78dc-356a-4fca-8877-068ee2f31824-7@apache.org"
decl_stmt|;
DECL|field|RESP_IMAGE_CID
specifier|static
specifier|final
name|String
name|RESP_IMAGE_CID
init|=
literal|"4c7b78dc-356a-4fca-8877-068ee2f31824-8@apache.org"
decl_stmt|;
DECL|field|RESP_MESSAGE
specifier|static
specifier|final
name|String
name|RESP_MESSAGE
init|=
literal|"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
operator|+
literal|"<DetailResponse xmlns=\"http://apache.org/camel/cxf/mtom_feature/types\">"
operator|+
literal|"<photo><xop:Include xmlns:xop=\"http://www.w3.org/2004/08/xop/include\""
operator|+
literal|" href=\"cid:"
operator|+
name|RESP_PHOTO_CID
operator|+
literal|"\"/>"
operator|+
literal|"</photo><image><xop:Include xmlns:xop=\"http://www.w3.org/2004/08/xop/include\""
operator|+
literal|" href=\"cid:"
operator|+
name|RESP_IMAGE_CID
operator|+
literal|"\"/></image></DetailResponse>"
decl_stmt|;
DECL|field|MTOM_DISABLED_RESP_MESSAGE
specifier|static
specifier|final
name|String
name|MTOM_DISABLED_RESP_MESSAGE
init|=
literal|"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
operator|+
literal|"<DetailResponse xmlns=\"http://apache.org/camel/cxf/mtom_feature/types\">"
operator|+
literal|"<photo>cid:"
operator|+
name|RESP_PHOTO_CID
operator|+
literal|"</photo>"
operator|+
literal|"<image>cid:"
operator|+
name|RESP_IMAGE_CID
operator|+
literal|"</image></DetailResponse>"
decl_stmt|;
DECL|field|requestJpeg
specifier|static
name|byte
index|[]
name|requestJpeg
decl_stmt|;
DECL|field|responseJpeg
specifier|static
name|byte
index|[]
name|responseJpeg
decl_stmt|;
static|static
block|{
try|try
block|{
name|requestJpeg
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|CxfMtomConsumerPayloadModeTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/java.jpg"
argument_list|)
argument_list|)
expr_stmt|;
name|responseJpeg
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|CxfMtomConsumerPayloadModeTest
operator|.
name|class
operator|.
name|getResourceAsStream
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
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|MtomTestHelper ()
specifier|private
name|MtomTestHelper
parameter_list|()
block|{
comment|// utility class
block|}
DECL|method|assertEquals (byte[] bytes1, byte[] bytes2)
specifier|static
name|void
name|assertEquals
parameter_list|(
name|byte
index|[]
name|bytes1
parameter_list|,
name|byte
index|[]
name|bytes2
parameter_list|)
block|{
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|bytes1
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|bytes2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|bytes1
operator|.
name|length
argument_list|,
name|bytes2
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bytes1
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|bytes1
index|[
name|i
index|]
argument_list|,
name|bytes2
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isAwtHeadless (org.apache.commons.logging.Log log, org.slf4j.Logger logger)
specifier|static
name|boolean
name|isAwtHeadless
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
name|log
parameter_list|,
name|org
operator|.
name|slf4j
operator|.
name|Logger
name|logger
parameter_list|)
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
literal|"Both loggers are not allowed to be null!"
argument_list|,
name|log
operator|==
literal|null
operator|&&
name|logger
operator|==
literal|null
argument_list|)
expr_stmt|;
name|boolean
name|headless
init|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"java.awt.headless"
argument_list|)
decl_stmt|;
if|if
condition|(
name|headless
condition|)
block|{
comment|// having the conversion characters %c{1} inside log4j.properties will reveal us the
comment|// test class currently running as we make use of it's logger to warn about skipping!
name|String
name|warning
init|=
literal|"Running headless. Skipping test as Images may not work."
decl_stmt|;
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|warning
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Running headless. Skipping test as Images may not work."
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|headless
return|;
block|}
block|}
end_class

end_unit

