begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.digitalocean.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|digitalocean
operator|.
name|producer
package|;
end_package

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|pojo
operator|.
name|Delete
import|;
end_import

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|pojo
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|pojo
operator|.
name|Keys
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
name|component
operator|.
name|digitalocean
operator|.
name|DigitalOceanConfiguration
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
name|component
operator|.
name|digitalocean
operator|.
name|DigitalOceanEndpoint
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
name|component
operator|.
name|digitalocean
operator|.
name|constants
operator|.
name|DigitalOceanHeaders
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

begin_comment
comment|/**  * The DigitalOcean producer for SSH Keys API.  */
end_comment

begin_class
DECL|class|DigitalOceanKeysProducer
specifier|public
class|class
name|DigitalOceanKeysProducer
extends|extends
name|DigitalOceanProducer
block|{
DECL|method|DigitalOceanKeysProducer (DigitalOceanEndpoint endpoint, DigitalOceanConfiguration configuration)
specifier|public
name|DigitalOceanKeysProducer
parameter_list|(
name|DigitalOceanEndpoint
name|endpoint
parameter_list|,
name|DigitalOceanConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
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
switch|switch
condition|(
name|determineOperation
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
case|case
name|list
case|:
name|getKeys
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|create
case|:
name|createKey
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|get
case|:
name|getKey
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|update
case|:
name|updateKey
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|delete
case|:
name|deleteKey
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported operation"
argument_list|)
throw|;
block|}
block|}
DECL|method|getKey (Exchange exchange)
specifier|private
name|void
name|getKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Integer
name|keyId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|ID
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|fingerprint
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|KEY_FINGERPRINT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Key
name|key
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|keyId
argument_list|)
condition|)
block|{
name|key
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|getKeyInfo
argument_list|(
name|keyId
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|fingerprint
argument_list|)
condition|)
block|{
name|key
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|getKeyInfo
argument_list|(
name|fingerprint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|DigitalOceanHeaders
operator|.
name|ID
operator|+
literal|" or "
operator|+
name|DigitalOceanHeaders
operator|.
name|KEY_FINGERPRINT
operator|+
literal|" must be specified"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Key [{}] "
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
DECL|method|getKeys (Exchange exchange)
specifier|private
name|void
name|getKeys
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Keys
name|keys
init|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|getAvailableKeys
argument_list|(
name|configuration
operator|.
name|getPage
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"All Keys : page {} [{}] "
argument_list|,
name|configuration
operator|.
name|getPage
argument_list|()
argument_list|,
name|keys
operator|.
name|getKeys
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|keys
operator|.
name|getKeys
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteKey (Exchange exchange)
specifier|private
name|void
name|deleteKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Integer
name|keyId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|ID
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|fingerprint
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|KEY_FINGERPRINT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Delete
name|delete
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|keyId
argument_list|)
condition|)
block|{
name|delete
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|deleteKey
argument_list|(
name|keyId
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|fingerprint
argument_list|)
condition|)
block|{
name|delete
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|deleteKey
argument_list|(
name|fingerprint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|DigitalOceanHeaders
operator|.
name|ID
operator|+
literal|" or "
operator|+
name|DigitalOceanHeaders
operator|.
name|KEY_FINGERPRINT
operator|+
literal|" must be specified"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Delete Key {}"
argument_list|,
name|delete
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|delete
argument_list|)
expr_stmt|;
block|}
DECL|method|createKey (Exchange exchange)
specifier|private
name|void
name|createKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Key
name|key
init|=
operator|new
name|Key
argument_list|()
decl_stmt|;
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
name|DigitalOceanHeaders
operator|.
name|NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|DigitalOceanHeaders
operator|.
name|NAME
operator|+
literal|" must be specified"
argument_list|)
throw|;
block|}
else|else
block|{
name|key
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|String
name|publicKey
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|KEY_PUBLIC_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|publicKey
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|DigitalOceanHeaders
operator|.
name|KEY_PUBLIC_KEY
operator|+
literal|" must be specified"
argument_list|)
throw|;
block|}
else|else
block|{
name|key
operator|.
name|setPublicKey
argument_list|(
name|publicKey
argument_list|)
expr_stmt|;
block|}
name|key
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|createKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Key created {}"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
DECL|method|updateKey (Exchange exchange)
specifier|private
name|void
name|updateKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Integer
name|keyId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|ID
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|fingerprint
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DigitalOceanHeaders
operator|.
name|KEY_FINGERPRINT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Key
name|key
decl_stmt|;
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
name|DigitalOceanHeaders
operator|.
name|NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|DigitalOceanHeaders
operator|.
name|NAME
operator|+
literal|" must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|keyId
argument_list|)
condition|)
block|{
name|key
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|updateKey
argument_list|(
name|keyId
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|fingerprint
argument_list|)
condition|)
block|{
name|key
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|updateKey
argument_list|(
name|fingerprint
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|DigitalOceanHeaders
operator|.
name|ID
operator|+
literal|" or "
operator|+
name|DigitalOceanHeaders
operator|.
name|KEY_FINGERPRINT
operator|+
literal|" must be specified"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Update Key [{}] "
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

