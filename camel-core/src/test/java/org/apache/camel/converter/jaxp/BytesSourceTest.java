begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxp
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
name|ContextTestSupport
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|BytesSourceTest
specifier|public
class|class
name|BytesSourceTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testBytesSourceCtr ()
specifier|public
name|void
name|testBytesSourceCtr
parameter_list|()
block|{
name|BytesSource
name|bs
init|=
operator|new
name|BytesSource
argument_list|(
literal|"foo"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bs
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BytesSource[foo]"
argument_list|,
name|bs
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|bs
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bs
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bs
operator|.
name|getReader
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBytesSourceCtrSystemId ()
specifier|public
name|void
name|testBytesSourceCtrSystemId
parameter_list|()
block|{
name|BytesSource
name|bs
init|=
operator|new
name|BytesSource
argument_list|(
literal|"foo"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"Camel"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bs
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BytesSource[foo]"
argument_list|,
name|bs
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|bs
operator|.
name|getSystemId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bs
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bs
operator|.
name|getReader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

