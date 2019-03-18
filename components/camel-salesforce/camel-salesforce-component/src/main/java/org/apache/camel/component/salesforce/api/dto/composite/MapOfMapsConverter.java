begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.composite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
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
name|Iterator
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
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|MarshallingContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|converters
operator|.
name|UnmarshallingContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|HierarchicalStreamWriter
import|;
end_import

begin_class
DECL|class|MapOfMapsConverter
specifier|public
class|class
name|MapOfMapsConverter
implements|implements
name|Converter
block|{
DECL|field|ATTRIBUTES_PROPERTY
specifier|private
specifier|static
specifier|final
name|String
name|ATTRIBUTES_PROPERTY
init|=
literal|"attributes"
decl_stmt|;
annotation|@
name|Override
DECL|method|canConvert (final Class type)
specifier|public
name|boolean
name|canConvert
parameter_list|(
specifier|final
name|Class
name|type
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|marshal (final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context)
specifier|public
name|void
name|marshal
parameter_list|(
specifier|final
name|Object
name|source
parameter_list|,
specifier|final
name|HierarchicalStreamWriter
name|writer
parameter_list|,
specifier|final
name|MarshallingContext
name|context
parameter_list|)
block|{
name|context
operator|.
name|convertAnother
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (final HierarchicalStreamReader reader, final UnmarshallingContext context)
specifier|public
name|Object
name|unmarshal
parameter_list|(
specifier|final
name|HierarchicalStreamReader
name|reader
parameter_list|,
specifier|final
name|UnmarshallingContext
name|context
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ret
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
name|reader
operator|.
name|hasMoreChildren
argument_list|()
condition|)
block|{
name|readMap
argument_list|(
name|reader
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
DECL|method|readMap (final HierarchicalStreamReader reader, final Map<String, Object> map)
name|Object
name|readMap
parameter_list|(
specifier|final
name|HierarchicalStreamReader
name|reader
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|reader
operator|.
name|hasMoreChildren
argument_list|()
condition|)
block|{
name|reader
operator|.
name|moveDown
argument_list|()
expr_stmt|;
specifier|final
name|String
name|key
init|=
name|reader
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Iterator
name|attributeNames
init|=
name|reader
operator|.
name|getAttributeNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|attributeNames
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|String
name|attributeName
init|=
operator|(
name|String
operator|)
name|attributeNames
operator|.
name|next
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|put
argument_list|(
name|attributeName
argument_list|,
name|reader
operator|.
name|getAttribute
argument_list|(
name|attributeName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Object
name|nested
init|=
name|readMap
argument_list|(
name|reader
argument_list|,
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|attributes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|nested
operator|instanceof
name|String
condition|)
block|{
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|newNested
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|newNested
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|nested
argument_list|)
expr_stmt|;
name|newNested
operator|.
name|put
argument_list|(
name|ATTRIBUTES_PROPERTY
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
name|nested
operator|=
name|newNested
expr_stmt|;
block|}
else|else
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|nestedMap
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|nested
decl_stmt|;
name|nestedMap
operator|.
name|put
argument_list|(
name|ATTRIBUTES_PROPERTY
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
block|}
block|}
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|nested
argument_list|)
expr_stmt|;
name|reader
operator|.
name|moveUp
argument_list|()
expr_stmt|;
name|readMap
argument_list|(
name|reader
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
name|reader
operator|.
name|getValue
argument_list|()
return|;
block|}
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

