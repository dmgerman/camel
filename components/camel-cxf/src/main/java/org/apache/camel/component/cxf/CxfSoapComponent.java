begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|impl
operator|.
name|DefaultComponent
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
name|util
operator|.
name|CamelContextHelper
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
name|util
operator|.
name|IntrospectionSupport
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://activemq.apache.org/camel/cxf.html">SOAP Component</a>  *  * @version $Revision: 576522 $  */
end_comment

begin_class
DECL|class|CxfSoapComponent
specifier|public
class|class
name|CxfSoapComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
name|soapProps
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"soap."
argument_list|)
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|remaining
operator|+=
literal|"?"
operator|+
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
name|Endpoint
name|endpoint
init|=
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|CxfSoapEndpoint
name|soapEndpoint
init|=
operator|new
name|CxfSoapEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|soapEndpoint
argument_list|,
name|soapProps
argument_list|)
expr_stmt|;
name|soapEndpoint
operator|.
name|init
argument_list|()
expr_stmt|;
return|return
name|soapEndpoint
return|;
block|}
annotation|@
name|Override
DECL|method|useIntrospectionOnEndpoint ()
specifier|protected
name|boolean
name|useIntrospectionOnEndpoint
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

