begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.soap.name
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|soap
operator|.
name|name
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
name|XmlSchema
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
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * Strategy to determine the marshalled element name by looking at the annotations of the  * class to be marshalled  */
end_comment

begin_class
DECL|class|TypeNameStrategy
specifier|public
class|class
name|TypeNameStrategy
implements|implements
name|ElementNameStrategy
block|{
comment|/**      * @return determine element name by using the XmlType.name() of the type to be      * marshalled and the XmlSchema.namespace() of the package-info      */
DECL|method|findQNameForSoapActionOrType (String soapAction, Class<?> type)
specifier|public
name|QName
name|findQNameForSoapActionOrType
parameter_list|(
name|String
name|soapAction
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|XmlType
name|xmlType
init|=
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlType
operator|==
literal|null
operator|||
name|xmlType
operator|.
name|name
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The type "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|" needs to have an XmlType annotation with name"
argument_list|)
throw|;
block|}
name|String
name|nameSpace
init|=
name|xmlType
operator|.
name|namespace
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"##default"
operator|.
name|equals
argument_list|(
name|nameSpace
argument_list|)
condition|)
block|{
name|XmlSchema
name|xmlSchema
init|=
name|type
operator|.
name|getPackage
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|XmlSchema
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlSchema
operator|!=
literal|null
condition|)
block|{
name|nameSpace
operator|=
name|xmlSchema
operator|.
name|namespace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
operator|new
name|QName
argument_list|(
name|nameSpace
argument_list|,
name|xmlType
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

