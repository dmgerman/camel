begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * The<code>Container</code> interface defines an object that can be used  * to customize all Camel contexts created.  *  * A container can be used to globally intercept and customize Camel contexts,  * by registering a<code>LifecycleStrategy</code>, a<code>ProcessorFactory</code>,  * or any other SPI object.  */
end_comment

begin_interface
DECL|interface|Container
specifier|public
interface|interface
name|Container
block|{
comment|/**      * The<code>Instance</code> class holds a<code>Container</code> singleton.      */
DECL|class|Instance
specifier|public
specifier|static
specifier|final
class|class
name|Instance
block|{
DECL|field|container
specifier|private
specifier|static
name|Container
name|container
decl_stmt|;
DECL|method|Instance ()
specifier|private
name|Instance
parameter_list|()
block|{         }
comment|/**          * Access the registered Container.          *          * @return the Container singleton          */
DECL|method|get ()
specifier|public
specifier|static
name|Container
name|get
parameter_list|()
block|{
return|return
name|container
return|;
block|}
comment|/**          * Register the Container.          *          * @param container the Container to register          */
DECL|method|set (Container container)
specifier|public
specifier|static
name|void
name|set
parameter_list|(
name|Container
name|container
parameter_list|)
block|{
name|Instance
operator|.
name|container
operator|=
name|container
expr_stmt|;
block|}
comment|/**          * Called by Camel when a<code>CamelContext</code> has been created.          *          * @param camelContext the newly created CamelContext          */
DECL|method|manage (CamelContext camelContext)
specifier|public
specifier|static
name|void
name|manage
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|Container
name|cnt
init|=
name|container
decl_stmt|;
if|if
condition|(
name|cnt
operator|!=
literal|null
condition|)
block|{
name|cnt
operator|.
name|manage
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Called by Camel when a<code>CamelContext</code> has been created.      *      * @param camelContext the newly created CamelContext      */
DECL|method|manage (CamelContext camelContext)
name|void
name|manage
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

