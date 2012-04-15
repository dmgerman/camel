begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.converter
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
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|CamelContext
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
name|Exchange
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ConverterTest
specifier|public
class|class
name|ConverterTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testToArray ()
specifier|public
name|void
name|testToArray
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|testList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|testList
operator|.
name|add
argument_list|(
literal|"string 1"
argument_list|)
expr_stmt|;
name|testList
operator|.
name|add
argument_list|(
literal|"string 2"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|array
init|=
name|CxfConverter
operator|.
name|toArray
argument_list|(
name|testList
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The array should not be null"
argument_list|,
name|array
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The array size should not be wrong"
argument_list|,
literal|2
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToInputStream ()
specifier|public
name|void
name|testToInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Response
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|response
operator|.
name|getEntity
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|InputStream
name|result
init|=
name|CxfConverter
operator|.
name|toInputStream
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the inputStream here "
argument_list|,
name|is
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reset
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|response
operator|.
name|getEntity
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|result
operator|=
name|CxfConverter
operator|.
name|toInputStream
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the inputStream here "
argument_list|,
name|result
operator|instanceof
name|ByteArrayInputStream
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

