begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|model
operator|.
name|rest
operator|.
name|RestParamType
import|;
end_import

begin_class
DECL|class|FromRestExplicitComponentTest
specifier|public
class|class
name|FromRestExplicitComponentTest
extends|extends
name|FromRestGetTest
block|{
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// configure to use dummy-rest
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"dummy-rest"
argument_list|)
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/say/hello"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:hello"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"dummy-rest"
argument_list|)
operator|.
name|path
argument_list|(
literal|"/say/bye"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|consumes
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|restParam
argument_list|()
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|header
argument_list|)
operator|.
name|description
argument_list|(
literal|"header param description1"
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"integer"
argument_list|)
operator|.
name|allowableValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|"3"
argument_list|,
literal|"4"
argument_list|)
argument_list|)
operator|.
name|defaultValue
argument_list|(
literal|"1"
argument_list|)
operator|.
name|allowMultiple
argument_list|(
literal|false
argument_list|)
operator|.
name|name
argument_list|(
literal|"header_count"
argument_list|)
operator|.
name|required
argument_list|(
literal|true
argument_list|)
operator|.
name|paramAccess
argument_list|(
literal|"acc1"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|restParam
argument_list|()
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|query
argument_list|)
operator|.
name|description
argument_list|(
literal|"header param description2"
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|allowableValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|)
argument_list|)
operator|.
name|defaultValue
argument_list|(
literal|"b"
argument_list|)
operator|.
name|allowMultiple
argument_list|(
literal|true
argument_list|)
operator|.
name|name
argument_list|(
literal|"header_letter"
argument_list|)
operator|.
name|required
argument_list|(
literal|false
argument_list|)
operator|.
name|paramAccess
argument_list|(
literal|"acc2"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|restResponseMsg
argument_list|()
operator|.
name|code
argument_list|(
literal|300
argument_list|)
operator|.
name|message
argument_list|(
literal|"test msg"
argument_list|)
operator|.
name|responseModel
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
operator|.
name|endResponseMsg
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|post
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:update"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bye"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

