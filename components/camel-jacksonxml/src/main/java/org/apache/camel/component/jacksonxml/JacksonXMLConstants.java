begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jacksonxml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jacksonxml
package|;
end_package

begin_class
DECL|class|JacksonXMLConstants
specifier|public
specifier|final
class|class
name|JacksonXMLConstants
block|{
DECL|field|ENABLE_TYPE_CONVERTER
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_TYPE_CONVERTER
init|=
literal|"CamelJacksonXmlEnableTypeConverter"
decl_stmt|;
DECL|field|ENABLE_XML_TYPE_CONVERTER
specifier|public
specifier|static
specifier|final
name|String
name|ENABLE_XML_TYPE_CONVERTER
init|=
literal|"CamelJacksonXmlEnableTypeConverter"
decl_stmt|;
DECL|field|UNMARSHAL_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|UNMARSHAL_TYPE
init|=
literal|"CamelJacksonXmlUnmarshalType"
decl_stmt|;
DECL|method|JacksonXMLConstants ()
specifier|private
name|JacksonXMLConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

