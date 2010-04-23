begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ode
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ode
package|;
end_package

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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ode
operator|.
name|bpel
operator|.
name|iapi
operator|.
name|EndpointReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ode
operator|.
name|bpel
operator|.
name|iapi
operator|.
name|EndpointReferenceContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|OdeEndpointReferenceContext
specifier|public
class|class
name|OdeEndpointReferenceContext
implements|implements
name|EndpointReferenceContext
block|{
DECL|method|resolveEndpointReference (Element element)
specifier|public
name|EndpointReference
name|resolveEndpointReference
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"resolveEndpointReference"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|convertEndpoint (QName qName, Element element)
specifier|public
name|EndpointReference
name|convertEndpoint
parameter_list|(
name|QName
name|qName
parameter_list|,
name|Element
name|element
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"convertEndpoint"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|getConfigLookup (EndpointReference endpointReference)
specifier|public
name|Map
name|getConfigLookup
parameter_list|(
name|EndpointReference
name|endpointReference
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"getConfigLookup"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

