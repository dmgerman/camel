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
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebFault
import|;
end_import

begin_comment
comment|/**  * Determine element name for an exception  */
end_comment

begin_class
DECL|class|ExceptionNameStrategy
specifier|public
class|class
name|ExceptionNameStrategy
implements|implements
name|ElementNameStrategy
block|{
comment|/**      * @return QName from exception class by evaluating the WebFault annotataion      */
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
name|WebFault
name|webFault
init|=
name|type
operator|.
name|getAnnotation
argument_list|(
name|WebFault
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|webFault
operator|==
literal|null
operator|||
name|webFault
operator|.
name|targetNamespace
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
literal|" needs to have an WebFault annotation with name and targetNamespace"
argument_list|)
throw|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|webFault
operator|.
name|targetNamespace
argument_list|()
argument_list|,
name|webFault
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

