begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|stream
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|ContextTestSupport
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ReaderCacheTest
specifier|public
class|class
name|ReaderCacheTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testReaderCache ()
specifier|public
name|void
name|testReaderCache
parameter_list|()
throws|throws
name|Exception
block|{
name|ReaderCache
name|cache
init|=
operator|new
name|ReaderCache
argument_list|(
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|cache
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|cache
operator|.
name|writeTo
argument_list|(
name|bos
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|bos
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|cache
argument_list|,
name|bos
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

