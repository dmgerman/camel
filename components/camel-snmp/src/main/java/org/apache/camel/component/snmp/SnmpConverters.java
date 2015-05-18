begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.snmp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|snmp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|Converter
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
name|snmp4j
operator|.
name|PDU
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|PDUv1
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|smi
operator|.
name|OID
import|;
end_import

begin_import
import|import
name|org
operator|.
name|snmp4j
operator|.
name|smi
operator|.
name|VariableBinding
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|SnmpConverters
specifier|public
specifier|final
class|class
name|SnmpConverters
block|{
DECL|field|SNMP_TAG
specifier|public
specifier|static
specifier|final
name|String
name|SNMP_TAG
init|=
literal|"snmp"
decl_stmt|;
DECL|field|ENTRY_TAG
specifier|public
specifier|static
specifier|final
name|String
name|ENTRY_TAG
init|=
literal|"entry"
decl_stmt|;
DECL|field|OID_TAG
specifier|public
specifier|static
specifier|final
name|String
name|OID_TAG
init|=
literal|"oid"
decl_stmt|;
DECL|field|VALUE_TAG
specifier|public
specifier|static
specifier|final
name|String
name|VALUE_TAG
init|=
literal|"value"
decl_stmt|;
DECL|field|SNMP_TAG_OPEN
specifier|private
specifier|static
specifier|final
name|String
name|SNMP_TAG_OPEN
init|=
literal|'<'
operator|+
name|SNMP_TAG
operator|+
literal|'>'
decl_stmt|;
DECL|field|SNMP_TAG_CLOSE
specifier|private
specifier|static
specifier|final
name|String
name|SNMP_TAG_CLOSE
init|=
literal|"</"
operator|+
name|SNMP_TAG
operator|+
literal|'>'
decl_stmt|;
DECL|field|ENTRY_TAG_OPEN
specifier|private
specifier|static
specifier|final
name|String
name|ENTRY_TAG_OPEN
init|=
literal|'<'
operator|+
name|ENTRY_TAG
operator|+
literal|'>'
decl_stmt|;
DECL|field|ENTRY_TAG_CLOSE
specifier|private
specifier|static
specifier|final
name|String
name|ENTRY_TAG_CLOSE
init|=
literal|"</"
operator|+
name|ENTRY_TAG
operator|+
literal|'>'
decl_stmt|;
DECL|field|OID_TAG_OPEN
specifier|private
specifier|static
specifier|final
name|String
name|OID_TAG_OPEN
init|=
literal|'<'
operator|+
name|OID_TAG
operator|+
literal|'>'
decl_stmt|;
DECL|field|OID_TAG_CLOSE
specifier|private
specifier|static
specifier|final
name|String
name|OID_TAG_CLOSE
init|=
literal|"</"
operator|+
name|OID_TAG
operator|+
literal|'>'
decl_stmt|;
DECL|field|VALUE_TAG_OPEN
specifier|private
specifier|static
specifier|final
name|String
name|VALUE_TAG_OPEN
init|=
literal|'<'
operator|+
name|VALUE_TAG
operator|+
literal|'>'
decl_stmt|;
DECL|field|VALUE_TAG_CLOSE
specifier|private
specifier|static
specifier|final
name|String
name|VALUE_TAG_CLOSE
init|=
literal|"</"
operator|+
name|VALUE_TAG
operator|+
literal|'>'
decl_stmt|;
DECL|method|SnmpConverters ()
specifier|private
name|SnmpConverters
parameter_list|()
block|{
comment|//Utility Class
block|}
annotation|@
name|Converter
comment|// Camel could use this method to convert the String into a List
DECL|method|toOIDList (String s, Exchange exchange)
specifier|public
specifier|static
name|OIDList
name|toOIDList
parameter_list|(
name|String
name|s
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|OIDList
name|list
init|=
operator|new
name|OIDList
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// seems to be a comma separated oid list
name|StringTokenizer
name|strTok
init|=
operator|new
name|StringTokenizer
argument_list|(
name|s
argument_list|,
literal|","
argument_list|)
decl_stmt|;
while|while
condition|(
name|strTok
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|tok
init|=
name|strTok
operator|.
name|nextToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|tok
operator|!=
literal|null
operator|&&
name|tok
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
operator|new
name|OID
argument_list|(
name|tok
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// empty token - skip
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
comment|// maybe a single oid
name|list
operator|.
name|add
argument_list|(
operator|new
name|OID
argument_list|(
name|s
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// return null if we can't convert without an error
comment|// and it could let camel to choice the other converter to do the job
comment|// new OID(...) will throw NumberFormatException if it's not a valid OID
return|return
literal|null
return|;
block|}
block|}
DECL|method|entryAppend (StringBuilder sb, String tag, String value)
specifier|private
specifier|static
name|void
name|entryAppend
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|tag
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|ENTRY_TAG_OPEN
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<"
operator|+
name|tag
operator|+
literal|">"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"</"
operator|+
name|tag
operator|+
literal|">"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|ENTRY_TAG_CLOSE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Converts the given snmp pdu to a String body.      *      * @param pdu       the snmp pdu      * @return  the text content      */
annotation|@
name|Converter
DECL|method|toString (PDU pdu)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|PDU
name|pdu
parameter_list|)
block|{
comment|// the output buffer
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// prepare the header
if|if
condition|(
name|pdu
operator|.
name|getType
argument_list|()
operator|==
name|PDU
operator|.
name|V1TRAP
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"<"
operator|+
name|SNMP_TAG
operator|+
literal|" messageType=\"v1\">"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|SNMP_TAG_OPEN
argument_list|)
expr_stmt|;
block|}
comment|// Extract SNMPv1 specific variables
if|if
condition|(
name|pdu
operator|.
name|getType
argument_list|()
operator|==
name|PDU
operator|.
name|V1TRAP
condition|)
block|{
name|PDUv1
name|v1pdu
init|=
operator|(
name|PDUv1
operator|)
name|pdu
decl_stmt|;
name|entryAppend
argument_list|(
name|sb
argument_list|,
literal|"enterprise"
argument_list|,
name|v1pdu
operator|.
name|getEnterprise
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|entryAppend
argument_list|(
name|sb
argument_list|,
literal|"agent-addr"
argument_list|,
name|v1pdu
operator|.
name|getAgentAddress
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|entryAppend
argument_list|(
name|sb
argument_list|,
literal|"generic-trap"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|v1pdu
operator|.
name|getGenericTrap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|entryAppend
argument_list|(
name|sb
argument_list|,
literal|"specific-trap"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|v1pdu
operator|.
name|getSpecificTrap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|entryAppend
argument_list|(
name|sb
argument_list|,
literal|"time-stamp"
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|v1pdu
operator|.
name|getTimestamp
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// now loop all variables of the response
for|for
control|(
name|Object
name|o
range|:
name|pdu
operator|.
name|getVariableBindings
argument_list|()
control|)
block|{
name|VariableBinding
name|b
init|=
operator|(
name|VariableBinding
operator|)
name|o
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|ENTRY_TAG_OPEN
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OID_TAG_OPEN
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|b
operator|.
name|getOid
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OID_TAG_CLOSE
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|VALUE_TAG_OPEN
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|getXmlSafeString
argument_list|(
name|b
operator|.
name|getVariable
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|VALUE_TAG_CLOSE
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|ENTRY_TAG_CLOSE
argument_list|)
expr_stmt|;
block|}
comment|// prepare the footer
name|sb
operator|.
name|append
argument_list|(
name|SNMP_TAG_CLOSE
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getXmlSafeString (String string)
specifier|private
specifier|static
name|String
name|getXmlSafeString
parameter_list|(
name|String
name|string
parameter_list|)
block|{
return|return
name|string
operator|.
name|replaceAll
argument_list|(
literal|"<"
argument_list|,
literal|"&lt;"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|">"
argument_list|,
literal|"&gt;"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"&"
argument_list|,
literal|"&amp;"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"\""
argument_list|,
literal|"&quot;"
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"'"
argument_list|,
literal|"&apos;"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

