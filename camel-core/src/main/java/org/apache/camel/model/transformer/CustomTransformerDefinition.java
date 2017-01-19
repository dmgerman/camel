begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.transformer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|transformer
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
name|spi
operator|.
name|Metadata
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
name|Transformer
import|;
end_import

begin_comment
comment|/**  * Represents a CustomTransformer. One of the bean reference (ref) or fully qualified class name (type)  * of the custom {@link Transformer} needs to be specified.  *   * {@see TransformerDefinition}  * {@see Transformer}  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"transformation"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"customTransformer"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CustomTransformerDefinition
specifier|public
class|class
name|CustomTransformerDefinition
extends|extends
name|TransformerDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
annotation|@
name|Override
DECL|method|doCreateTransformer (CamelContext context)
specifier|protected
name|Transformer
name|doCreateTransformer
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|ref
operator|==
literal|null
operator|&&
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"'ref' or 'type' must be specified for customTransformer"
argument_list|)
throw|;
block|}
name|Transformer
name|transformer
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|transformer
operator|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|ref
argument_list|,
name|Transformer
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|transformer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find transformer with ref:"
operator|+
name|ref
argument_list|)
throw|;
block|}
if|if
condition|(
name|transformer
operator|.
name|getModel
argument_list|()
operator|!=
literal|null
operator|||
name|transformer
operator|.
name|getFrom
argument_list|()
operator|!=
literal|null
operator|||
name|transformer
operator|.
name|getTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Transformer '%s' is already in use. Please check if duplicate transformer exists."
argument_list|,
name|ref
argument_list|)
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|Class
argument_list|<
name|Transformer
argument_list|>
name|transformerClass
init|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|type
argument_list|,
name|Transformer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|transformerClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find transformer class: "
operator|+
name|type
argument_list|)
throw|;
block|}
name|transformer
operator|=
name|context
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|transformerClass
argument_list|)
expr_stmt|;
block|}
name|transformer
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|transformer
operator|.
name|setModel
argument_list|(
name|getScheme
argument_list|()
argument_list|)
operator|.
name|setFrom
argument_list|(
name|getFromType
argument_list|()
argument_list|)
operator|.
name|setTo
argument_list|(
name|getToType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
comment|/**      * Set a bean reference of the Transformer      *      * @param ref the bean reference of the Transformer      */
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Set a class name of the Transformer      *      * @param type the class name of the Transformer      */
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
block|}
end_class

end_unit

