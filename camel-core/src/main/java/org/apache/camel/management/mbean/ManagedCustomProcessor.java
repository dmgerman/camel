begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
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
name|ProcessorDefinition
import|;
end_import

begin_comment
comment|/**  * A managed custom processor is a processor which implements the {@link org.apache.camel.spi.ManagementAware}  * interface.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ManagedCustomProcessor
specifier|public
class|class
name|ManagedCustomProcessor
extends|extends
name|ManagedProcessor
block|{
DECL|field|managedObject
specifier|private
specifier|final
name|Object
name|managedObject
decl_stmt|;
DECL|method|ManagedCustomProcessor (CamelContext context, Object managedObject, Processor processor, ProcessorDefinition<?> definition)
specifier|public
name|ManagedCustomProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Object
name|managedObject
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|this
operator|.
name|managedObject
operator|=
name|managedObject
expr_stmt|;
block|}
DECL|method|getManagedObject ()
specifier|public
name|Object
name|getManagedObject
parameter_list|()
block|{
return|return
name|managedObject
return|;
block|}
block|}
end_class

end_unit

