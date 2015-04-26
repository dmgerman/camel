begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openshift
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openshift
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|ApplicationScale
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|IApplication
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|IDomain
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|IGear
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|IGearGroup
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|OpenShiftException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|cartridge
operator|.
name|IDeployedStandaloneCartridge
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|cartridge
operator|.
name|IEmbeddableCartridge
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|cartridge
operator|.
name|IEmbeddedCartridge
import|;
end_import

begin_import
import|import
name|com
operator|.
name|openshift
operator|.
name|client
operator|.
name|cartridge
operator|.
name|query
operator|.
name|LatestEmbeddableCartridge
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
name|CamelExchangeException
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
name|Exchange
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
name|DefaultProducer
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
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|OpenShiftProducer
specifier|public
class|class
name|OpenShiftProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|TIMESTAMP_FORMAT
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP_FORMAT
init|=
literal|"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
decl_stmt|;
DECL|method|OpenShiftProducer (Endpoint endpoint)
specifier|public
name|OpenShiftProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|OpenShiftEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|OpenShiftEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|openshiftServer
init|=
name|OpenShiftHelper
operator|.
name|getOpenShiftServer
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|IDomain
name|domain
init|=
name|OpenShiftHelper
operator|.
name|loginAndGetDomain
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|openshiftServer
argument_list|)
decl_stmt|;
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"User has no domain with id "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getDomain
argument_list|()
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|OpenShiftOperation
name|operation
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|OPERATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getOperation
argument_list|()
argument_list|,
name|OpenShiftOperation
operator|.
name|class
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|start
case|:
name|doStart
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|stop
case|:
name|doStop
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|restart
case|:
name|doRestart
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|state
case|:
name|doState
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|getStandaloneCartridge
case|:
name|doGetStandaloneCartridge
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|getEmbeddedCartridges
case|:
name|doGetEmbeddedCartridges
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|getGitUrl
case|:
name|doGetGitUrl
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|addEmbeddedCartridge
case|:
name|doAddEmbeddedCartridge
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|removeEmbeddedCartridge
case|:
name|doRemoveEmbeddedCartridge
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|scaleUp
case|:
name|doScaleUp
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|scaleDown
case|:
name|doScaleDown
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
break|break;
case|case
name|list
case|:
default|default:
comment|// and do list by default
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getMode
argument_list|()
operator|.
name|equals
argument_list|(
literal|"json"
argument_list|)
condition|)
block|{
name|doListJson
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doListPojo
argument_list|(
name|exchange
argument_list|,
name|domain
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
DECL|method|doListJson (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doListJson
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"{\n  \"applications\": ["
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|IApplication
name|application
range|:
name|domain
operator|.
name|getApplications
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\n    ],"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
name|String
name|date
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|TIMESTAMP_FORMAT
argument_list|)
operator|.
name|format
argument_list|(
name|application
operator|.
name|getCreationTime
argument_list|()
argument_list|)
decl_stmt|;
comment|// application
name|sb
operator|.
name|append
argument_list|(
literal|"\n    {"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"uuid\": \""
operator|+
name|application
operator|.
name|getUUID
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"domain\": \""
operator|+
name|application
operator|.
name|getDomain
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"name\": \""
operator|+
name|application
operator|.
name|getName
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"creationTime\": \""
operator|+
name|date
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"applicationUrl\": \""
operator|+
name|application
operator|.
name|getApplicationUrl
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"gitUrl\": \""
operator|+
name|application
operator|.
name|getGitUrl
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"sshUrl\": \""
operator|+
name|application
operator|.
name|getSshUrl
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
comment|// catridge
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"catridge\": {"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n        \"name\": \""
operator|+
name|application
operator|.
name|getCartridge
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n        \"displayName\": \""
operator|+
name|application
operator|.
name|getCartridge
argument_list|()
operator|.
name|getDisplayName
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n        \"description\": \""
operator|+
name|application
operator|.
name|getCartridge
argument_list|()
operator|.
name|getDescription
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      },"
argument_list|)
expr_stmt|;
comment|// embedded catridges
name|List
argument_list|<
name|IEmbeddedCartridge
argument_list|>
name|embeddedCartridges
init|=
name|application
operator|.
name|getEmbeddedCartridges
argument_list|()
decl_stmt|;
if|if
condition|(
name|embeddedCartridges
operator|!=
literal|null
operator|&&
operator|!
name|embeddedCartridges
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"embeddedCatridges\": ["
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|IEmbeddedCartridge
argument_list|>
name|it
init|=
name|embeddedCartridges
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|IEmbeddedCartridge
name|cartridge
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"catridge\": {"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n        \"name\": \""
operator|+
name|cartridge
operator|.
name|getName
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n        \"displayName\": \""
operator|+
name|cartridge
operator|.
name|getDisplayName
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n        \"description\": \""
operator|+
name|cartridge
operator|.
name|getDescription
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      }"
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n      ]"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"gearProfile\": \""
operator|+
name|application
operator|.
name|getGearProfile
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n      \"gears\": ["
argument_list|)
expr_stmt|;
name|boolean
name|firstGear
init|=
literal|true
decl_stmt|;
for|for
control|(
name|IGearGroup
name|group
range|:
name|application
operator|.
name|getGearGroups
argument_list|()
control|)
block|{
for|for
control|(
name|IGear
name|gear
range|:
name|group
operator|.
name|getGears
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|firstGear
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|firstGear
operator|=
literal|false
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n        {"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n         \"id\": \""
operator|+
name|gear
operator|.
name|getId
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n         \"sshUrl\": \""
operator|+
name|gear
operator|.
name|getSshUrl
argument_list|()
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n         \"state\": \""
operator|+
name|gear
operator|.
name|getState
argument_list|()
operator|.
name|getState
argument_list|()
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n        }"
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n      ]"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n    }"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n  ]"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n}"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doListPojo (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doListPojo
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|domain
operator|.
name|getApplications
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doStart (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doStart
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|app
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doStop (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doStop
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|app
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doRestart (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doRestart
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|app
operator|.
name|restart
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doState (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doState
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|String
name|state
init|=
name|OpenShiftHelper
operator|.
name|getStateForApplication
argument_list|(
name|app
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doGetStandaloneCartridge (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doGetStandaloneCartridge
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|IDeployedStandaloneCartridge
name|p
init|=
name|app
operator|.
name|getCartridge
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|p
operator|.
name|getDisplayName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doGetEmbeddedCartridges (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doGetEmbeddedCartridges
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|List
argument_list|<
name|IEmbeddedCartridge
argument_list|>
name|p
init|=
name|app
operator|.
name|getEmbeddedCartridges
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doAddEmbeddedCartridge (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doAddEmbeddedCartridge
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|String
name|embeddedCartridgeName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|EMBEDDED_CARTRIDGE_NAME
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|embeddedCartridgeName
argument_list|)
operator|&&
name|embeddedCartridgeName
operator|!=
literal|null
condition|)
block|{
name|IEmbeddedCartridge
name|p
init|=
name|app
operator|.
name|addEmbeddableCartridge
argument_list|(
operator|(
operator|new
name|LatestEmbeddableCartridge
argument_list|(
name|embeddedCartridgeName
argument_list|)
operator|)
operator|.
name|get
argument_list|(
name|app
argument_list|)
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|p
operator|.
name|getDisplayName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Cartridge not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|doRemoveEmbeddedCartridge (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doRemoveEmbeddedCartridge
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|String
name|embeddedCartridgeName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|EMBEDDED_CARTRIDGE_NAME
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|embeddedCartridgeName
argument_list|)
operator|&&
name|embeddedCartridgeName
operator|!=
literal|null
condition|)
block|{
name|IEmbeddableCartridge
name|removingCartridge
init|=
operator|(
operator|new
name|LatestEmbeddableCartridge
argument_list|(
name|embeddedCartridgeName
argument_list|)
operator|)
operator|.
name|get
argument_list|(
name|app
argument_list|)
decl_stmt|;
for|for
control|(
name|IEmbeddedCartridge
name|cartridge
range|:
name|app
operator|.
name|getEmbeddedCartridges
argument_list|()
control|)
block|{
if|if
condition|(
name|cartridge
operator|.
name|equals
argument_list|(
name|removingCartridge
argument_list|)
condition|)
block|{
name|cartridge
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|cartridge
operator|.
name|getDisplayName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Cartridge not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|doScaleUp (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doScaleUp
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
try|try
block|{
name|app
operator|.
name|scaleUp
argument_list|()
expr_stmt|;
name|ApplicationScale
name|result
init|=
name|app
operator|.
name|getApplicationScale
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OpenShiftException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" is not scalable"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|doScaleDown (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doScaleDown
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|ApplicationScale
name|scale
init|=
name|app
operator|.
name|getApplicationScale
argument_list|()
decl_stmt|;
if|if
condition|(
name|scale
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|ApplicationScale
operator|.
name|NO_SCALE
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Scaling on application with id "
operator|+
name|name
operator|+
literal|" is not enabled"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|app
operator|.
name|scaleDown
argument_list|()
expr_stmt|;
name|ApplicationScale
name|result
init|=
name|app
operator|.
name|getApplicationScale
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|doGetGitUrl (Exchange exchange, IDomain domain)
specifier|protected
name|void
name|doGetGitUrl
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|IDomain
name|domain
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|String
name|name
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|OpenShiftConstants
operator|.
name|APPLICATION
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getApplication
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application not specified"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|IApplication
name|app
init|=
name|domain
operator|.
name|getApplicationByName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|app
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Application with id "
operator|+
name|name
operator|+
literal|" not found."
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
name|String
name|gitUrl
init|=
name|app
operator|.
name|getGitUrl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|gitUrl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

