begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox.demo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
operator|.
name|demo
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

begin_class
DECL|class|SimpleRouteBuilder
specifier|public
class|class
name|SimpleRouteBuilder
extends|extends
name|RouteBuilder
block|{
comment|/* (non-Javadoc)      * @see org.apache.camel.builder.RouteBuilder#configure()      */
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
comment|// Routes
name|from
argument_list|(
literal|"seda:addToCatalog"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:Received request for seda:addToCatalog"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:library?method=addToCatalog"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:findBook"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:Received request for seda:findBook"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:library?method=findBook"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:findAuthor"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:Received request for seda:findAuthor"
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:library?method=findAuthor"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

