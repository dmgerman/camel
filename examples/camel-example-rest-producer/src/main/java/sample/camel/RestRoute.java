begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|sample.camel
package|package
name|sample
operator|.
name|camel
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * A Camel route that calls the REST service using a timer  *<p/>  * Use<tt>@Component</tt> to make Camel auto detect this route when starting.  */
end_comment

begin_class
annotation|@
name|Component
DECL|class|RestRoute
specifier|public
class|class
name|RestRoute
extends|extends
name|RouteBuilder
block|{
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
comment|// call the embedded rest service from the PetController
name|restConfiguration
argument_list|()
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|port
argument_list|(
literal|8080
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer:hello?period={{timer.period}}"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
name|simple
argument_list|(
literal|"${random(1,3)}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"rest:get:pets/{id}"
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

