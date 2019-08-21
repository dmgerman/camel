begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|XmlElementRef
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
name|model
operator|.
name|OptionalIdentifiedDefinition
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * A series of rest services defined using the rest-dsl  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"rest"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"rests"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RestsDefinition
specifier|public
class|class
name|RestsDefinition
extends|extends
name|OptionalIdentifiedDefinition
argument_list|<
name|RestsDefinition
argument_list|>
implements|implements
name|RestContainer
block|{
annotation|@
name|XmlElementRef
DECL|field|rests
specifier|private
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|rests
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|RestsDefinition ()
specifier|public
name|RestsDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Rests: "
operator|+
name|rests
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"rests"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"Rest "
operator|+
name|getId
argument_list|()
return|;
block|}
comment|// Properties
comment|// -----------------------------------------------------------------------
annotation|@
name|Override
DECL|method|getRests ()
specifier|public
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|getRests
parameter_list|()
block|{
return|return
name|rests
return|;
block|}
comment|/**      * The rest services      */
annotation|@
name|Override
DECL|method|setRests (List<RestDefinition> rests)
specifier|public
name|void
name|setRests
parameter_list|(
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|rests
parameter_list|)
block|{
name|this
operator|.
name|rests
operator|=
name|rests
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Creates a rest DSL      */
DECL|method|rest ()
specifier|public
name|RestDefinition
name|rest
parameter_list|()
block|{
name|RestDefinition
name|rest
init|=
name|createRest
argument_list|()
decl_stmt|;
return|return
name|rest
argument_list|(
name|rest
argument_list|)
return|;
block|}
comment|/**      * Creates a rest DSL      *      * @param uri the rest path      */
DECL|method|rest (String uri)
specifier|public
name|RestDefinition
name|rest
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|RestDefinition
name|rest
init|=
name|createRest
argument_list|()
decl_stmt|;
name|rest
operator|.
name|setPath
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|rest
argument_list|(
name|rest
argument_list|)
return|;
block|}
comment|/**      * Adds the {@link org.apache.camel.model.rest.RestsDefinition}      */
DECL|method|rest (RestDefinition rest)
specifier|public
name|RestDefinition
name|rest
parameter_list|(
name|RestDefinition
name|rest
parameter_list|)
block|{
name|getRests
argument_list|()
operator|.
name|add
argument_list|(
name|rest
argument_list|)
expr_stmt|;
return|return
name|rest
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|createRest ()
specifier|protected
name|RestDefinition
name|createRest
parameter_list|()
block|{
name|RestDefinition
name|rest
init|=
operator|new
name|RestDefinition
argument_list|()
decl_stmt|;
return|return
name|rest
return|;
block|}
block|}
end_class

end_unit

