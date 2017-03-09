begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|cxf
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
name|BeanInject
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

begin_class
DECL|class|BeanInjectRouteBuilder
specifier|public
class|class
name|BeanInjectRouteBuilder
extends|extends
name|RouteBuilder
block|{
annotation|@
name|BeanInject
DECL|field|simpleBean
specifier|private
name|SimpleBean
name|simpleBean
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"cxf:bean:routerEndpoint"
argument_list|)
operator|.
name|bean
argument_list|(
name|simpleBean
argument_list|)
comment|// does nothing
operator|.
name|transform
argument_list|(
name|simple
argument_list|(
literal|"scheme: ${headers.CamelCxfMessage.get(HTTP.REQUEST).scheme}"
operator|+
literal|", x-forwarded-proto: ${headers.CamelCxfMessage.get(HTTP.REQUEST).getHeader(X-Forwarded-Proto)}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

