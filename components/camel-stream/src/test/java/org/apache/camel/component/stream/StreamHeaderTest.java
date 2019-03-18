begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|builder
operator|.
name|RouteBuilder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test when using custom output stream.  */
end_comment

begin_class
DECL|class|StreamHeaderTest
specifier|public
class|class
name|StreamHeaderTest
extends|extends
name|CamelTestSupport
block|{
comment|// START SNIPPET: e1
DECL|field|mystream
specifier|private
name|OutputStream
name|mystream
init|=
operator|new
name|MyOutputStream
argument_list|()
decl_stmt|;
DECL|field|sb
specifier|private
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testStringContent ()
specifier|public
name|void
name|testStringContent
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
comment|// StreamProducer appends \n in text mode
name|assertEquals
argument_list|(
literal|"Hello"
operator|+
name|LS
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBinaryContent ()
specifier|public
name|void
name|testBinaryContent
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
literal|"Hello"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
comment|// StreamProducer is in binary mode so no \n is appended
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"stream"
argument_list|,
name|constant
argument_list|(
name|mystream
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"stream:header"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyOutputStream
specifier|private
class|class
name|MyOutputStream
extends|extends
name|OutputStream
block|{
DECL|method|write (int b)
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|b
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

