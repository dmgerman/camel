begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ContextTestSupport
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
name|builder
operator|.
name|RouteBuilder
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|XPathRouteConcurrentBigTest
specifier|public
class|class
name|XPathRouteConcurrentBigTest
extends|extends
name|ContextTestSupport
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
name|XPathRouteConcurrentBigTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|XMLTEST1
specifier|private
specifier|static
specifier|final
name|String
name|XMLTEST1
init|=
literal|"<message><messageType>AAA</messageType><sender>0123456789101112131415</sender>"
operator|+
literal|"<rawData>Uyw7TSVkUMxUyw7TSgGUMQAyw7TSVkUMxUyA7TSgGUMQAyw7TSVkUMxUyA</rawData>"
operator|+
literal|"<sentDate>2009-10-12T12:22:02+02:00</sentDate><receivedDate>2009-10-12T12:23:31.248+02:00</receivedDate>"
operator|+
literal|"<intproperty>1</intproperty><stringproperty>aaaaaaabbbbbbbccccccccdddddddd</stringproperty></message>"
decl_stmt|;
DECL|field|XMLTEST2
specifier|private
specifier|static
specifier|final
name|String
name|XMLTEST2
init|=
literal|"<message><messageType>AAB</messageType><sender>0123456789101112131415</sender>"
operator|+
literal|"<rawData>Uyw7TSVkUMxUyw7TSgGUMQAyw7TSVkUMxUyA7TSgGUMQAyw7TSVkUMxUyA</rawData>"
operator|+
literal|"<sentDate>2009-10-12T12:22:02+02:00</sentDate><receivedDate>2009-10-12T12:23:31.248+02:00</receivedDate>"
operator|+
literal|"<intproperty>1</intproperty><stringproperty>aaaaaaabbbbbbbccccccccdddddddd</stringproperty></message>"
decl_stmt|;
DECL|field|XMLTEST3
specifier|private
specifier|static
specifier|final
name|String
name|XMLTEST3
init|=
literal|"<message><messageType>ZZZ</messageType><sender>0123456789101112131415</sender>"
operator|+
literal|"<rawData>Uyw7TSVkUMxUyw7TSgGUMQAyw7TSVkUMxUyA7TSgGUMQAyw7TSVkUMxUyA</rawData>"
operator|+
literal|"<sentDate>2009-10-12T12:22:02+02:00</sentDate><receivedDate>2009-10-12T12:23:31.248+02:00</receivedDate>"
operator|+
literal|"<intproperty>1</intproperty><stringproperty>aaaaaaabbbbbbbccccccccdddddddd</stringproperty></message>"
decl_stmt|;
DECL|method|testConcurrent ()
specifier|public
name|void
name|testConcurrent
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|333
argument_list|)
expr_stmt|;
block|}
DECL|method|doSendMessages (int messageCount)
specifier|private
name|void
name|doSendMessages
parameter_list|(
name|int
name|messageCount
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Sending "
operator|+
name|messageCount
operator|+
literal|" messages"
argument_list|)
expr_stmt|;
name|int
name|forResult
init|=
operator|(
name|messageCount
operator|*
literal|2
operator|/
literal|3
operator|)
operator|+
name|messageCount
operator|%
literal|3
decl_stmt|;
name|int
name|forOther
init|=
name|messageCount
operator|-
name|forResult
decl_stmt|;
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
comment|// give more time on slow servers
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|setResultWaitTime
argument_list|(
literal|30000
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:other"
argument_list|)
operator|.
name|setResultWaitTime
argument_list|(
literal|30000
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|forResult
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:other"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|forOther
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
switch|switch
condition|(
name|i
operator|%
literal|3
condition|)
block|{
case|case
literal|0
case|:
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|XMLTEST1
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|XMLTEST2
argument_list|)
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
name|XMLTEST3
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Sent "
operator|+
name|messageCount
operator|+
literal|" messages in "
operator|+
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|now
operator|)
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Processed "
operator|+
name|messageCount
operator|+
literal|" messages in "
operator|+
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|now
operator|)
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"seda:foo?concurrentConsumers=50&size=250000"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"//messageType = 'AAA' or "
operator|+
literal|"//messageType = 'AAB' or "
operator|+
literal|"//messageType = 'AAC' or "
operator|+
literal|"//messageType = 'AAD' or "
operator|+
literal|"//messageType = 'AAE' or "
operator|+
literal|"//messageType = 'AAF' or "
operator|+
literal|"//messageType = 'AAG' or "
operator|+
literal|"//messageType = 'AAH' or "
operator|+
literal|"//messageType = 'AAI' or "
operator|+
literal|"//messageType = 'AAJ' or "
operator|+
literal|"//messageType = 'AAK' or "
operator|+
literal|"//messageType = 'AAL' or "
operator|+
literal|"//messageType = 'AAM' or "
operator|+
literal|"//messageType = 'AAN' or "
operator|+
literal|"//messageType = 'AAO' or "
operator|+
literal|"//messageType = 'AAP' or "
operator|+
literal|"//messageType = 'AAQ' or "
operator|+
literal|"//messageType = 'AAR' or "
operator|+
literal|"//messageType = 'AAS' or "
operator|+
literal|"//messageType = 'AAT' or "
operator|+
literal|"//messageType = 'AAU' or "
operator|+
literal|"//messageType = 'AAV' or "
operator|+
literal|"//messageType = 'AAW' or "
operator|+
literal|"//messageType = 'AAX' or "
operator|+
literal|"//messageType = 'AAY' or "
operator|+
literal|"//messageType = 'AAZ'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

