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
comment|/**  * Simply ElementNameStrategy that returns one preset QName. This can be handy  * for simple asynchronous calls  */
end_comment

begin_class
DECL|class|QNameStrategy
specifier|public
class|class
name|QNameStrategy
implements|implements
name|ElementNameStrategy
block|{
DECL|field|elementName
specifier|private
name|QName
name|elementName
decl_stmt|;
comment|/**      * Initialize with one QName      *       * @param elmentName      *            QName to be used for all finds      */
DECL|method|QNameStrategy (QName elmentName)
specifier|public
name|QNameStrategy
parameter_list|(
name|QName
name|elmentName
parameter_list|)
block|{
name|this
operator|.
name|elementName
operator|=
name|elmentName
expr_stmt|;
block|}
comment|/**      * @return preset element name      */
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
return|return
name|elementName
return|;
block|}
DECL|method|findExceptionForFaultName (QName faultName)
specifier|public
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
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Exception lookup is not supported for QNameStrategy"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

