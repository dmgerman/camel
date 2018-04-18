begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * Value object to hold information about a method in a JAX-WS service interface.  * Method can have many parameters in the signature, but only one response object.  */
end_comment

begin_class
DECL|class|MethodInfo
specifier|public
specifier|final
class|class
name|MethodInfo
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|soapAction
specifier|private
name|String
name|soapAction
decl_stmt|;
DECL|field|in
specifier|private
name|TypeInfo
index|[]
name|in
decl_stmt|;
DECL|field|out
specifier|private
name|TypeInfo
name|out
decl_stmt|;
comment|// map of type name to qname
DECL|field|inTypeMap
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|TypeInfo
argument_list|>
name|inTypeMap
decl_stmt|;
comment|/**      * Initialize       *       * @param name method name      * @param soapAction      * @param in input parameters      * @param out return types      */
DECL|method|MethodInfo (String name, String soapAction, TypeInfo[] in, TypeInfo out)
specifier|public
name|MethodInfo
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|soapAction
parameter_list|,
name|TypeInfo
index|[]
name|in
parameter_list|,
name|TypeInfo
name|out
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|soapAction
operator|=
name|soapAction
expr_stmt|;
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|inTypeMap
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|TypeInfo
name|typeInfo
range|:
name|in
control|)
block|{
if|if
condition|(
name|inTypeMap
operator|.
name|containsKey
argument_list|(
name|typeInfo
operator|.
name|getTypeName
argument_list|()
argument_list|)
operator|&&
operator|(
operator|!
operator|(
name|typeInfo
operator|.
name|getTypeName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"javax.xml.ws.Holder"
argument_list|)
operator|)
operator|)
operator|&&
operator|(
operator|!
operator|(
name|inTypeMap
operator|.
name|get
argument_list|(
name|typeInfo
operator|.
name|getTypeName
argument_list|()
argument_list|)
operator|.
name|equals
argument_list|(
name|typeInfo
operator|.
name|getElName
argument_list|()
argument_list|)
operator|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Ambiguous QName mapping. The type [ "
operator|+
name|typeInfo
operator|.
name|getTypeName
argument_list|()
operator|+
literal|" ] is already mapped to a QName in this method."
operator|+
literal|" This is not supported."
argument_list|)
throw|;
block|}
name|inTypeMap
operator|.
name|put
argument_list|(
name|typeInfo
operator|.
name|getTypeName
argument_list|()
argument_list|,
name|typeInfo
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getSoapAction ()
specifier|public
name|String
name|getSoapAction
parameter_list|()
block|{
return|return
name|soapAction
return|;
block|}
DECL|method|getIn ()
specifier|public
name|TypeInfo
index|[]
name|getIn
parameter_list|()
block|{
return|return
name|in
return|;
block|}
DECL|method|getOut ()
specifier|public
name|TypeInfo
name|getOut
parameter_list|()
block|{
return|return
name|out
return|;
block|}
DECL|method|getIn (String typeName)
specifier|public
name|TypeInfo
name|getIn
parameter_list|(
name|String
name|typeName
parameter_list|)
block|{
return|return
name|this
operator|.
name|inTypeMap
operator|.
name|get
argument_list|(
name|typeName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

