begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.common.header
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
operator|.
name|common
operator|.
name|header
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|spi
operator|.
name|HeaderFilterStrategy
operator|.
name|Direction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapBindingConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapBindingFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapVersion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapVersionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|headers
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A {@link MessageHeaderFilter} to drop all SOAP headers.  */
end_comment

begin_class
DECL|class|SoapMessageHeaderFilter
specifier|public
class|class
name|SoapMessageHeaderFilter
implements|implements
name|MessageHeaderFilter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SoapMessageHeaderFilter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ACTIVATION_NS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|ACTIVATION_NS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP11_BINDING_ID
argument_list|,
name|SoapBindingFactory
operator|.
name|SOAP_11_BINDING
argument_list|,
name|SoapBindingFactory
operator|.
name|SOAP_12_BINDING
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|getActivationNamespaces ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getActivationNamespaces
parameter_list|()
block|{
return|return
name|ACTIVATION_NS
return|;
block|}
annotation|@
name|Override
DECL|method|filter (Direction direction, List<Header> headers)
specifier|public
name|void
name|filter
parameter_list|(
name|Direction
name|direction
parameter_list|,
name|List
argument_list|<
name|Header
argument_list|>
name|headers
parameter_list|)
block|{
comment|// Treat both in and out direction the same
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Iterator
argument_list|<
name|Header
argument_list|>
name|iterator
init|=
name|headers
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Header
name|header
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing header: {}"
argument_list|,
name|header
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|header
operator|instanceof
name|SoapHeader
operator|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Skipped header: {} since it is not a SoapHeader"
argument_list|,
name|header
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|SoapHeader
name|soapHeader
init|=
name|SoapHeader
operator|.
name|class
operator|.
name|cast
argument_list|(
name|header
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|SoapVersion
argument_list|>
name|itv
init|=
name|SoapVersionFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|getVersions
argument_list|()
init|;
name|itv
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|SoapVersion
name|version
init|=
name|itv
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|soapHeader
operator|.
name|getActor
argument_list|()
operator|!=
literal|null
operator|&&
name|soapHeader
operator|.
name|getActor
argument_list|()
operator|.
name|equals
argument_list|(
name|version
operator|.
name|getNextRole
argument_list|()
argument_list|)
condition|)
block|{
comment|// dropping headers if actor/role equals to {ns}/role|actor/next
comment|// cxf SoapHeader needs to have soap:header@relay attribute,
comment|// then we can check for it here as well
name|LOG
operator|.
name|trace
argument_list|(
literal|"Filtered header: {}"
argument_list|,
name|header
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

