begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * If a configuration bean needs to be aware of the point in the XML file  * in which its defined then it can implement this method to have the  * XML {@link Element} node injected so that it can grab the namespace context  * or look at local comments etc.  *  * @version $Revision: 1.1 $  */
end_comment

begin_interface
DECL|interface|ElementAware
specifier|public
interface|interface
name|ElementAware
block|{
comment|/**      * Injects the XML Element which defines this bean so that it can      * analyse the namespace context or grab the local comments etc.      *      * @param element the XML element      */
DECL|method|setElement (Element element)
name|void
name|setElement
parameter_list|(
name|Element
name|element
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

