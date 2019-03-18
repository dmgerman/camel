begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.soap.name
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
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

begin_comment
comment|/**  * Strategy interface for determining the element name for a SOAP body or fault  */
end_comment

begin_interface
DECL|interface|ElementNameStrategy
specifier|public
interface|interface
name|ElementNameStrategy
block|{
comment|/**      * Deterimine element name for given type      *       * @param soapAction      * @param type      * @return resolved element name      */
DECL|method|findQNameForSoapActionOrType (String soapAction, Class<?> type)
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
function_decl|;
comment|/**      * Determine exception class for given SOAP Fault QName      * @param faultName      * @return      */
DECL|method|findExceptionForFaultName (QName faultName)
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|findExceptionForFaultName
parameter_list|(
name|QName
name|faultName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

