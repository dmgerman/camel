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

begin_comment
comment|/**  * Value object to hold information about a method in a JAX-WS service interface  */
end_comment

begin_class
DECL|class|MethodInfo
specifier|final
class|class
name|MethodInfo
block|{
DECL|field|soapAction
specifier|private
name|String
name|soapAction
decl_stmt|;
DECL|field|in
specifier|private
name|TypeInfo
name|in
decl_stmt|;
DECL|field|out
specifier|private
name|TypeInfo
name|out
decl_stmt|;
comment|/**      * Initialize       *       * @param soapAction      * @param in input parameter (document style so only one parameter)      * @param out return type      */
DECL|method|MethodInfo (String soapAction, TypeInfo in, TypeInfo out)
specifier|public
name|MethodInfo
parameter_list|(
name|String
name|soapAction
parameter_list|,
name|TypeInfo
name|in
parameter_list|,
name|TypeInfo
name|out
parameter_list|)
block|{
name|super
argument_list|()
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
block|}
end_class

end_unit

