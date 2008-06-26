begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|CamelException
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
name|processor
operator|.
name|ThrowFaultProcessor
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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;throwFault/&gt; element  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"throwFault"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ThrowFaultType
specifier|public
class|class
name|ThrowFaultType
extends|extends
name|ProcessorType
argument_list|<
name|ThrowFaultType
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|fault
specifier|private
name|Throwable
name|fault
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|faultRef
specifier|private
name|String
name|faultRef
decl_stmt|;
DECL|method|ThrowFaultType ()
specifier|public
name|ThrowFaultType
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"throwFault"
return|;
block|}
DECL|method|setFault (Throwable fault)
specifier|public
name|void
name|setFault
parameter_list|(
name|Throwable
name|fault
parameter_list|)
block|{
name|this
operator|.
name|fault
operator|=
name|fault
expr_stmt|;
block|}
DECL|method|getFault ()
specifier|public
name|Throwable
name|getFault
parameter_list|()
block|{
return|return
name|fault
return|;
block|}
DECL|method|setFaultRef (String ref)
specifier|public
name|void
name|setFaultRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|faultRef
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|getFaultRef ()
specifier|public
name|String
name|getFaultRef
parameter_list|()
block|{
return|return
name|faultRef
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|fault
operator|==
literal|null
condition|)
block|{
name|fault
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|faultRef
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|fault
operator|==
literal|null
condition|)
block|{
comment|// can't find the fault instance, create a new one
name|fault
operator|=
operator|new
name|CamelException
argument_list|(
name|faultRef
argument_list|)
expr_stmt|;
block|}
block|}
name|processor
operator|=
operator|new
name|ThrowFaultProcessor
argument_list|(
name|fault
argument_list|)
expr_stmt|;
block|}
return|return
name|processor
return|;
block|}
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
block|}
end_class

end_unit

