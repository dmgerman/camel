begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jackson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.component.jackson.JacksonDataFormat} that is using a list  */
end_comment

begin_class
DECL|class|ListJacksonDataFormat
specifier|public
class|class
name|ListJacksonDataFormat
extends|extends
name|JacksonDataFormat
block|{
DECL|method|ListJacksonDataFormat ()
specifier|public
name|ListJacksonDataFormat
parameter_list|()
block|{
name|useList
argument_list|()
expr_stmt|;
block|}
DECL|method|ListJacksonDataFormat (Class<?> unmarshalType)
specifier|public
name|ListJacksonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|super
argument_list|(
name|unmarshalType
argument_list|)
expr_stmt|;
name|useList
argument_list|()
expr_stmt|;
block|}
DECL|method|ListJacksonDataFormat (Class<?> unmarshalType, Class<?> jsonView)
specifier|public
name|ListJacksonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|)
block|{
name|super
argument_list|(
name|unmarshalType
argument_list|,
name|jsonView
argument_list|)
expr_stmt|;
name|useList
argument_list|()
expr_stmt|;
block|}
DECL|method|ListJacksonDataFormat (Class<?> unmarshalType, Class<?> jsonView, boolean enableJaxbAnnotationModule)
specifier|public
name|ListJacksonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|,
name|boolean
name|enableJaxbAnnotationModule
parameter_list|)
block|{
name|super
argument_list|(
name|unmarshalType
argument_list|,
name|jsonView
argument_list|,
name|enableJaxbAnnotationModule
argument_list|)
expr_stmt|;
name|useList
argument_list|()
expr_stmt|;
block|}
DECL|method|ListJacksonDataFormat (ObjectMapper mapper, Class<?> unmarshalType)
specifier|public
name|ListJacksonDataFormat
parameter_list|(
name|ObjectMapper
name|mapper
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|super
argument_list|(
name|mapper
argument_list|,
name|unmarshalType
argument_list|)
expr_stmt|;
name|useList
argument_list|()
expr_stmt|;
block|}
DECL|method|ListJacksonDataFormat (ObjectMapper mapper, Class<?> unmarshalType, Class<?> jsonView)
specifier|public
name|ListJacksonDataFormat
parameter_list|(
name|ObjectMapper
name|mapper
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|)
block|{
name|super
argument_list|(
name|mapper
argument_list|,
name|unmarshalType
argument_list|,
name|jsonView
argument_list|)
expr_stmt|;
name|useList
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

