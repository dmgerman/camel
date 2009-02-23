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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|XmlID
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|CollapsedStringAdapter
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
name|adapters
operator|.
name|XmlJavaTypeAdapter
import|;
end_import

begin_comment
comment|/**  * Allows an element to have an optional ID specified  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"optionalIdentifiedType"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|OptionalIdentifiedType
specifier|public
specifier|abstract
class|class
name|OptionalIdentifiedType
parameter_list|<
name|T
extends|extends
name|OptionalIdentifiedType
parameter_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|nodeCounters
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
name|nodeCounters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|AtomicInteger
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|CollapsedStringAdapter
operator|.
name|class
argument_list|)
annotation|@
name|XmlID
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|description
specifier|private
name|Description
name|description
decl_stmt|;
comment|/**      * Gets the value of the id property.      */
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
comment|/**      * Sets the value of the id property.      */
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
block|}
DECL|method|getDescription ()
specifier|public
name|Description
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (Description description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|Description
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
comment|/**      * Returns a short name for this node which can be useful for ID generation or referring to related resources like images      *      * @return defaults to "node" but derived nodes should overload this to provide a unique name      */
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"node"
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the description of this node      *      * @param id  sets the id, use null to not set an id      * @param text  sets the text description, use null to not set a text      * @param lang  sets the language for the description, use null to not set a language      * @return the builder      */
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
name|Description
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
name|Description
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
comment|/**      * Gets the node id, creating one if not already set.      */
DECL|method|idOrCreate ()
specifier|public
name|String
name|idOrCreate
parameter_list|()
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|setId
argument_list|(
name|createId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|getId
argument_list|()
return|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * A helper method to create a new id for this node      */
DECL|method|createId ()
specifier|protected
name|String
name|createId
parameter_list|()
block|{
name|String
name|key
init|=
name|getShortName
argument_list|()
decl_stmt|;
return|return
name|key
operator|+
name|getNodeCounter
argument_list|(
name|key
argument_list|)
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
comment|/**      * Returns the counter for the given node key, lazily creating one if necessary      */
DECL|method|getNodeCounter (String key)
specifier|protected
specifier|static
specifier|synchronized
name|AtomicInteger
name|getNodeCounter
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|AtomicInteger
name|answer
init|=
name|nodeCounters
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|nodeCounters
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

