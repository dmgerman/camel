begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dns.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dns
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|NetworkInterface
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|javax
operator|.
name|naming
operator|.
name|NamingEnumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|InitialDirContext
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
comment|/*   * Check if a hostname resolves to a specified cname or an ip  */
end_comment

begin_class
DECL|class|DnsActivation
specifier|public
class|class
name|DnsActivation
block|{
DECL|field|DNS_TYPES
specifier|private
specifier|static
specifier|final
specifier|transient
name|String
index|[]
name|DNS_TYPES
init|=
block|{
literal|"CNAME"
block|,
literal|"A"
block|}
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DnsActivation
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
DECL|field|resolvesTo
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|resolvesTo
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|DnsActivation ()
specifier|public
name|DnsActivation
parameter_list|()
block|{     }
DECL|method|DnsActivation (String hostname, List<String> resolvesTo)
specifier|public
name|DnsActivation
parameter_list|(
name|String
name|hostname
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|resolvesTo
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
name|this
operator|.
name|resolvesTo
operator|.
name|addAll
argument_list|(
name|resolvesTo
argument_list|)
expr_stmt|;
block|}
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
DECL|method|setResolvesTo (List<String> resolvesTo)
specifier|public
name|void
name|setResolvesTo
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|resolvesTo
parameter_list|)
block|{
name|this
operator|.
name|resolvesTo
operator|.
name|addAll
argument_list|(
name|resolvesTo
argument_list|)
expr_stmt|;
block|}
DECL|method|setResolvesTo (String resolvesTo)
specifier|public
name|void
name|setResolvesTo
parameter_list|(
name|String
name|resolvesTo
parameter_list|)
block|{
name|this
operator|.
name|resolvesTo
operator|.
name|add
argument_list|(
name|resolvesTo
argument_list|)
expr_stmt|;
block|}
DECL|method|isActive ()
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
if|if
condition|(
name|resolvesTo
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|resolvesTo
operator|.
name|addAll
argument_list|(
name|getLocalIps
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to get local ips and resolvesTo not specified. Identifying as inactive."
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Resolving "
operator|+
name|hostname
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|hostnames
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|hostnames
operator|.
name|add
argument_list|(
name|hostname
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|resolved
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|hostnames
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|NamingEnumeration
name|attributeEnumeration
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|hostname
init|=
name|hostnames
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|InetAddress
name|inetAddress
init|=
name|InetAddress
operator|.
name|getByName
argument_list|(
name|hostname
argument_list|)
decl_stmt|;
name|InitialDirContext
name|initialDirContext
init|=
operator|new
name|InitialDirContext
argument_list|()
decl_stmt|;
name|Attributes
name|attributes
init|=
name|initialDirContext
operator|.
name|getAttributes
argument_list|(
literal|"dns:/"
operator|+
name|inetAddress
operator|.
name|getHostName
argument_list|()
argument_list|,
name|DNS_TYPES
argument_list|)
decl_stmt|;
name|attributeEnumeration
operator|=
name|attributes
operator|.
name|getAll
argument_list|()
expr_stmt|;
while|while
condition|(
name|attributeEnumeration
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|Attribute
name|attribute
init|=
operator|(
name|Attribute
operator|)
name|attributeEnumeration
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|attribute
operator|.
name|getID
argument_list|()
decl_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|attribute
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|resolvesTo
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|id
operator|+
literal|" = "
operator|+
name|value
operator|+
literal|" matched. Identifying as active."
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
name|id
operator|+
literal|" = "
operator|+
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
literal|"CNAME"
argument_list|)
operator|&&
operator|!
name|resolved
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|hostnames
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|resolved
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|hostname
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|attributeEnumeration
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|attributeEnumeration
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to close attributeEnumeration. Memory leak possible."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|attributeEnumeration
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|getLocalIps ()
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getLocalIps
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|localIps
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|NetworkInterface
argument_list|>
name|networkInterfacesEnumeration
init|=
name|NetworkInterface
operator|.
name|getNetworkInterfaces
argument_list|()
decl_stmt|;
while|while
condition|(
name|networkInterfacesEnumeration
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|NetworkInterface
name|networkInterface
init|=
name|networkInterfacesEnumeration
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|Enumeration
argument_list|<
name|InetAddress
argument_list|>
name|inetAddressesEnumeration
init|=
name|networkInterface
operator|.
name|getInetAddresses
argument_list|()
decl_stmt|;
while|while
condition|(
name|inetAddressesEnumeration
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|InetAddress
name|inetAddress
init|=
name|inetAddressesEnumeration
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|ip
init|=
name|inetAddress
operator|.
name|getHostAddress
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Local ip: "
operator|+
name|ip
argument_list|)
expr_stmt|;
name|localIps
operator|.
name|add
argument_list|(
name|ip
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|localIps
return|;
block|}
block|}
end_class

end_unit

