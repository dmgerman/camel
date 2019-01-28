begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ssh
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ssh
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|InvalidKeySpecException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|CamelContext
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
name|support
operator|.
name|ResourceHelper
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|keyverifier
operator|.
name|ServerKeyVerifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|client
operator|.
name|session
operator|.
name|ClientSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|util
operator|.
name|Arrays
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
comment|/**  * ServerKeyVerifier that takes a camel resource as input file to validate the server key against.  *  */
end_comment

begin_class
DECL|class|ResourceBasedSSHKeyVerifier
specifier|public
class|class
name|ResourceBasedSSHKeyVerifier
implements|implements
name|ServerKeyVerifier
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|failOnUnknownHost
specifier|private
name|boolean
name|failOnUnknownHost
decl_stmt|;
DECL|field|knownHostsResource
specifier|private
name|String
name|knownHostsResource
decl_stmt|;
DECL|method|ResourceBasedSSHKeyVerifier (CamelContext camelContext, String knownHostsResource)
specifier|public
name|ResourceBasedSSHKeyVerifier
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|knownHostsResource
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|knownHostsResource
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|ResourceBasedSSHKeyVerifier (CamelContext camelContext, String knownHostsResource, boolean failOnUnknownHost)
specifier|public
name|ResourceBasedSSHKeyVerifier
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|knownHostsResource
parameter_list|,
name|boolean
name|failOnUnknownHost
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|knownHostsResource
operator|=
name|knownHostsResource
expr_stmt|;
name|this
operator|.
name|failOnUnknownHost
operator|=
name|failOnUnknownHost
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|verifyServerKey (ClientSession sshClientSession, SocketAddress remoteAddress, PublicKey serverKey)
specifier|public
name|boolean
name|verifyServerKey
parameter_list|(
name|ClientSession
name|sshClientSession
parameter_list|,
name|SocketAddress
name|remoteAddress
parameter_list|,
name|PublicKey
name|serverKey
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Trying to find known_hosts file %s"
argument_list|,
name|knownHostsResource
argument_list|)
expr_stmt|;
name|InputStream
name|knownHostsInputStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|knownHostsInputStream
operator|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
argument_list|,
name|knownHostsResource
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|possibleTokens
init|=
name|getKnownHostsFileTokensForSocketAddress
argument_list|(
name|remoteAddress
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Trying to mach PublicKey against provided known_hosts file"
argument_list|)
expr_stmt|;
name|PublicKey
name|matchingKey
init|=
name|findKeyForServerToken
argument_list|(
name|knownHostsInputStream
argument_list|,
name|possibleTokens
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchingKey
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Found PublicKey match for server"
argument_list|)
expr_stmt|;
name|boolean
name|match
init|=
name|Arrays
operator|.
name|areEqual
argument_list|(
name|matchingKey
operator|.
name|getEncoded
argument_list|()
argument_list|,
name|serverKey
operator|.
name|getEncoded
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|match
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioException
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Could not find known_hosts file %s"
argument_list|,
name|knownHostsResource
argument_list|)
argument_list|,
name|ioException
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|knownHostsInputStream
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|failOnUnknownHost
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not find matching key for client session, connection will fail due to configuration"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not find matching key for client session, connection will continue anyway due to configuration"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|method|findKeyForServerToken (InputStream knownHostsInputStream, List<String> possibleTokens)
specifier|private
name|PublicKey
name|findKeyForServerToken
parameter_list|(
name|InputStream
name|knownHostsInputStream
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|possibleTokens
parameter_list|)
block|{
name|String
name|knowHostsLines
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|knownHostsInputStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|knowHostsLines
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Could not read from the known_hosts file input stream"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
for|for
control|(
name|String
name|s
range|:
name|knowHostsLines
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
control|)
block|{
name|String
index|[]
name|parts
init|=
name|s
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|3
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Found malformed entry in known_hosts file"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|String
name|entry
init|=
name|parts
index|[
literal|0
index|]
decl_stmt|;
name|String
name|key
init|=
name|parts
index|[
literal|2
index|]
decl_stmt|;
for|for
control|(
name|String
name|serverToken
range|:
name|possibleTokens
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|contains
argument_list|(
name|serverToken
argument_list|)
condition|)
block|{
try|try
block|{
return|return
name|loadKey
argument_list|(
name|key
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
decl||
name|InvalidKeySpecException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Could not load key for server token %s"
argument_list|,
name|entry
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getKnownHostsFileTokensForSocketAddress (SocketAddress remoteAddress)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getKnownHostsFileTokensForSocketAddress
parameter_list|(
name|SocketAddress
name|remoteAddress
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|returnList
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|remoteAddress
operator|instanceof
name|InetSocketAddress
condition|)
block|{
name|InetSocketAddress
name|inetSocketAddress
init|=
operator|(
name|InetSocketAddress
operator|)
name|remoteAddress
decl_stmt|;
name|String
name|hostName
init|=
name|inetSocketAddress
operator|.
name|getHostName
argument_list|()
decl_stmt|;
name|String
name|ipAddress
init|=
name|inetSocketAddress
operator|.
name|getAddress
argument_list|()
operator|.
name|getHostAddress
argument_list|()
decl_stmt|;
name|String
name|remotePort
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|inetSocketAddress
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|returnList
operator|.
name|add
argument_list|(
name|hostName
argument_list|)
expr_stmt|;
name|returnList
operator|.
name|add
argument_list|(
literal|"["
operator|+
name|hostName
operator|+
literal|"]:"
operator|+
name|remotePort
argument_list|)
expr_stmt|;
name|returnList
operator|.
name|add
argument_list|(
name|ipAddress
argument_list|)
expr_stmt|;
name|returnList
operator|.
name|add
argument_list|(
literal|"["
operator|+
name|ipAddress
operator|+
literal|"]:"
operator|+
name|remotePort
argument_list|)
expr_stmt|;
block|}
return|return
name|returnList
return|;
block|}
comment|/*      * Decode the public key string, which is a base64 encoded string that consists      * of multiple parts: 1. public key type (ssh-rsa, ssh-dss, ...) 2. binary key      * data (May consists of multiple parts)      *       * Each part is composed by two sub-parts 1. Length of the part (4 bytes) 2.      * Binary part (length as defined by 1.)      *       * Uses SSHPublicKeyHolder to construct the actual PublicKey Object      *       * Note: Currently only supports RSA and DSA Public keys as required by      * https://tools.ietf.org/html/rfc4253#section-6.6      *       */
DECL|method|loadKey (String key)
specifier|private
name|PublicKey
name|loadKey
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|NoSuchAlgorithmException
throws|,
name|InvalidKeySpecException
block|{
name|SSHPublicKeyHolder
name|sshPublicKeyHolder
init|=
operator|new
name|SSHPublicKeyHolder
argument_list|()
decl_stmt|;
name|byte
index|[]
name|keyByteArray
init|=
name|Base64
operator|.
name|getDecoder
argument_list|()
operator|.
name|decode
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|int
name|keyByteArrayCursor
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|tmpData
init|=
operator|new
name|byte
index|[
literal|4
index|]
decl_stmt|;
name|int
name|tmpCursor
init|=
literal|0
decl_stmt|;
name|boolean
name|getLengthMode
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|keyByteArrayCursor
operator|<
name|keyByteArray
operator|.
name|length
condition|)
block|{
if|if
condition|(
name|getLengthMode
condition|)
block|{
if|if
condition|(
name|tmpCursor
operator|<
literal|4
condition|)
block|{
name|tmpData
index|[
name|tmpCursor
index|]
operator|=
name|keyByteArray
index|[
name|keyByteArrayCursor
index|]
expr_stmt|;
name|tmpCursor
operator|++
expr_stmt|;
name|keyByteArrayCursor
operator|++
expr_stmt|;
continue|continue;
block|}
else|else
block|{
name|tmpCursor
operator|=
literal|0
expr_stmt|;
name|getLengthMode
operator|=
literal|false
expr_stmt|;
name|tmpData
operator|=
operator|new
name|byte
index|[
name|byteArrayToInt
argument_list|(
name|tmpData
argument_list|)
index|]
expr_stmt|;
block|}
block|}
name|tmpData
index|[
name|tmpCursor
index|]
operator|=
name|keyByteArray
index|[
name|keyByteArrayCursor
index|]
expr_stmt|;
name|tmpCursor
operator|++
expr_stmt|;
name|keyByteArrayCursor
operator|++
expr_stmt|;
if|if
condition|(
name|tmpCursor
operator|==
name|tmpData
operator|.
name|length
condition|)
block|{
name|sshPublicKeyHolder
operator|.
name|push
argument_list|(
name|tmpData
argument_list|)
expr_stmt|;
name|getLengthMode
operator|=
literal|true
expr_stmt|;
name|tmpData
operator|=
operator|new
name|byte
index|[
literal|4
index|]
expr_stmt|;
name|tmpCursor
operator|=
literal|0
expr_stmt|;
block|}
block|}
return|return
name|sshPublicKeyHolder
operator|.
name|toPublicKey
argument_list|()
return|;
block|}
DECL|method|byteArrayToInt (byte[] tmpData)
specifier|private
name|int
name|byteArrayToInt
parameter_list|(
name|byte
index|[]
name|tmpData
parameter_list|)
block|{
return|return
operator|new
name|BigInteger
argument_list|(
name|tmpData
argument_list|)
operator|.
name|intValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

