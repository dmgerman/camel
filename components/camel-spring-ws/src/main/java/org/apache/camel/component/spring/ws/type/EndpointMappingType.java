begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.type
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|type
package|;
end_package

begin_comment
comment|/**  * Endpoint mappings supported by consumer through uri configuration.  */
end_comment

begin_enum
DECL|enum|EndpointMappingType
specifier|public
enum|enum
name|EndpointMappingType
block|{
DECL|enumConstant|ROOT_QNAME
name|ROOT_QNAME
argument_list|(
literal|"rootqname:"
argument_list|)
block|,
DECL|enumConstant|ACTION
name|ACTION
argument_list|(
literal|"action:"
argument_list|)
block|,
DECL|enumConstant|TO
name|TO
argument_list|(
literal|"to:"
argument_list|)
block|,
DECL|enumConstant|SOAP_ACTION
name|SOAP_ACTION
argument_list|(
literal|"soapaction:"
argument_list|)
block|,
DECL|enumConstant|XPATHRESULT
name|XPATHRESULT
argument_list|(
literal|"xpathresult:"
argument_list|)
block|,
DECL|enumConstant|URI
name|URI
argument_list|(
literal|"uri:"
argument_list|)
block|,
DECL|enumConstant|BEANNAME
name|BEANNAME
argument_list|(
literal|"beanname:"
argument_list|)
block|;
DECL|field|prefix
specifier|private
name|String
name|prefix
decl_stmt|;
DECL|method|EndpointMappingType (String prefix)
specifier|private
name|EndpointMappingType
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
DECL|method|getPrefix ()
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
comment|/**      * Find {@link EndpointMappingType} that corresponds with the prefix of the      * given uri. Matching of uri prefix against enum values is case-insensitive      *      * @param uri remaining uri part of Spring-WS component      * @return EndpointMappingType corresponding to uri prefix      */
DECL|method|getTypeFromUriPrefix (String uri)
specifier|public
specifier|static
name|EndpointMappingType
name|getTypeFromUriPrefix
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|EndpointMappingType
name|type
range|:
name|EndpointMappingType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|uri
operator|.
name|toLowerCase
argument_list|()
operator|.
name|startsWith
argument_list|(
name|type
operator|.
name|getPrefix
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|type
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_enum

end_unit

