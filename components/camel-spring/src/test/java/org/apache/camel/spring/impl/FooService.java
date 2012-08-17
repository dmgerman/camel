begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|impl
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
name|Consume
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
name|Produce
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
name|ProducerTemplate
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|FooService
specifier|public
class|class
name|FooService
block|{
DECL|field|fooEndpoint
specifier|private
name|String
name|fooEndpoint
decl_stmt|;
DECL|field|barEndpoint
specifier|private
name|String
name|barEndpoint
decl_stmt|;
annotation|@
name|Produce
DECL|field|bar
specifier|private
name|ProducerTemplate
name|bar
decl_stmt|;
DECL|method|getFooEndpoint ()
specifier|public
name|String
name|getFooEndpoint
parameter_list|()
block|{
return|return
name|fooEndpoint
return|;
block|}
DECL|method|setFooEndpoint (String fooEndpoint)
specifier|public
name|void
name|setFooEndpoint
parameter_list|(
name|String
name|fooEndpoint
parameter_list|)
block|{
name|this
operator|.
name|fooEndpoint
operator|=
name|fooEndpoint
expr_stmt|;
block|}
DECL|method|getBarEndpoint ()
specifier|public
name|String
name|getBarEndpoint
parameter_list|()
block|{
return|return
name|barEndpoint
return|;
block|}
DECL|method|setBarEndpoint (String barEndpoint)
specifier|public
name|void
name|setBarEndpoint
parameter_list|(
name|String
name|barEndpoint
parameter_list|)
block|{
name|this
operator|.
name|barEndpoint
operator|=
name|barEndpoint
expr_stmt|;
block|}
annotation|@
name|Consume
DECL|method|onFoo (String input)
specifier|public
name|void
name|onFoo
parameter_list|(
name|String
name|input
parameter_list|)
block|{
name|bar
operator|.
name|sendBody
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

