begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|XmlElement
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
name|XmlType
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
name|NamedNode
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
name|NodeIdFactory
import|;
end_import

begin_comment
comment|/**  * Allows an element to have an optional ID specified  */
end_comment

begin_class
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"optionalIdentifiedDefinition"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|PROPERTY
argument_list|)
comment|// must use XmlAccessType.PROPERTY which is required by camel-spring / camel-blueprint for their namespace parsers
DECL|class|OptionalIdentifiedDefinition
specifier|public
specifier|abstract
class|class
name|OptionalIdentifiedDefinition
parameter_list|<
name|T
extends|extends
name|OptionalIdentifiedDefinition
parameter_list|<
name|T
parameter_list|>
parameter_list|>
implements|implements
name|NamedNode
implements|,
name|DefinitionPropertyPlaceholderConfigurer
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|customId
specifier|private
name|Boolean
name|customId
decl_stmt|;
DECL|field|description
specifier|private
name|DescriptionDefinition
name|description
decl_stmt|;
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * Sets the id of this node      */
annotation|@
name|XmlAttribute
DECL|method|setId (String value)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|value
expr_stmt|;
name|customId
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|DescriptionDefinition
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
comment|/**      * Sets the description of this node      *      * @param description sets the text description, use null to not set a text      */
annotation|@
name|XmlElement
DECL|method|setDescription (DescriptionDefinition description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|DescriptionDefinition
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParent ()
specifier|public
name|NamedNode
name|getParent
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the description of this node      *      * @param text sets the text description, use null to not set a text      * @return the builder      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|description (String text)
specifier|public
name|T
name|description
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|description
operator|==
literal|null
condition|)
block|{
name|description
operator|=
operator|new
name|DescriptionDefinition
argument_list|()
expr_stmt|;
block|}
name|description
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|this
return|;
block|}
comment|/**      * Sets the description of this node      *      * @param id sets the id, use null to not set an id      * @param text sets the text description, use null to not set a text      * @param lang sets the language for the description, use null to not set a      *            language      * @return the builder      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|description (String id, String text, String lang)
specifier|public
name|T
name|description
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|text
parameter_list|,
name|String
name|lang
parameter_list|)
block|{
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|description
operator|==
literal|null
condition|)
block|{
name|description
operator|=
operator|new
name|DescriptionDefinition
argument_list|()
expr_stmt|;
block|}
name|description
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|lang
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|description
operator|==
literal|null
condition|)
block|{
name|description
operator|=
operator|new
name|DescriptionDefinition
argument_list|()
expr_stmt|;
block|}
name|description
operator|.
name|setLang
argument_list|(
name|lang
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|this
return|;
block|}
comment|/**      * Sets the id of this node.      *<p/>      *<b>Important:</b> If you want to set the id of the route, then you      *<b>must</b> use<tt>routeId(String)</tt> instead.      *      * @param id the id      * @return the builder      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|id (String id)
specifier|public
name|T
name|id
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
operator|(
name|T
operator|)
name|this
return|;
block|}
comment|/**      * Gets the node id, creating one if not already set.      */
DECL|method|idOrCreate (NodeIdFactory factory)
specifier|public
name|String
name|idOrCreate
parameter_list|(
name|NodeIdFactory
name|factory
parameter_list|)
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
name|factory
operator|.
name|createId
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|id
return|;
block|}
DECL|method|getCustomId ()
specifier|public
name|Boolean
name|getCustomId
parameter_list|()
block|{
return|return
name|customId
return|;
block|}
comment|/**      * Whether the node id was explicit set, or was auto generated by Camel.      */
annotation|@
name|XmlAttribute
DECL|method|setCustomId (Boolean customId)
specifier|public
name|void
name|setCustomId
parameter_list|(
name|Boolean
name|customId
parameter_list|)
block|{
name|this
operator|.
name|customId
operator|=
name|customId
expr_stmt|;
block|}
comment|/**      * Returns whether a custom id has been assigned      */
DECL|method|hasCustomIdAssigned ()
specifier|public
name|boolean
name|hasCustomIdAssigned
parameter_list|()
block|{
return|return
name|customId
operator|!=
literal|null
operator|&&
name|customId
return|;
block|}
comment|/**      * Returns the description text or null if there is no description text      * associated with this node      */
annotation|@
name|Override
DECL|method|getDescriptionText ()
specifier|public
name|String
name|getDescriptionText
parameter_list|()
block|{
return|return
operator|(
name|description
operator|!=
literal|null
operator|)
condition|?
name|description
operator|.
name|getText
argument_list|()
else|:
literal|null
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
block|}
end_class

end_unit

