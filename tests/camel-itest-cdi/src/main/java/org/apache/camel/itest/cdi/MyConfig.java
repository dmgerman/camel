begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
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
name|cdi
operator|.
name|ContextName
import|;
end_import

begin_comment
comment|/**  * Instantiate a number of route builders using  */
end_comment

begin_class
DECL|class|MyConfig
specifier|public
class|class
name|MyConfig
block|{
annotation|@
name|Produces
annotation|@
name|ContextName
argument_list|(
literal|"contextE"
argument_list|)
DECL|method|createRouteA ()
specifier|public
name|RouteBuilder
name|createRouteA
parameter_list|()
block|{
return|return
operator|new
name|MyRouteBuilder
argument_list|(
literal|"seda:E.a"
argument_list|,
literal|"mock:E.b"
argument_list|)
return|;
block|}
annotation|@
name|Produces
annotation|@
name|ContextName
argument_list|(
literal|"contextE"
argument_list|)
DECL|method|createRouteB ()
specifier|public
name|RouteBuilder
name|createRouteB
parameter_list|()
block|{
return|return
operator|new
name|MyRouteBuilder
argument_list|(
literal|"seda:E.c"
argument_list|,
literal|"mock:E.d"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

