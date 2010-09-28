begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quickfixj.examples.transform
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quickfixj
operator|.
name|examples
operator|.
name|transform
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
name|quickfixj
operator|.
name|QuickfixjEndpoint
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|ConfigError
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|DataDictionary
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|quickfix
operator|.
name|SessionID
import|;
end_import

begin_class
DECL|class|QuickfixjEventJsonTransformer
specifier|public
class|class
name|QuickfixjEventJsonTransformer
block|{
DECL|field|renderer
specifier|private
specifier|final
name|QuickfixjMessageJsonTransformer
name|renderer
decl_stmt|;
DECL|method|QuickfixjEventJsonTransformer ()
specifier|public
name|QuickfixjEventJsonTransformer
parameter_list|()
throws|throws
name|ConfigError
block|{
name|renderer
operator|=
operator|new
name|QuickfixjMessageJsonTransformer
argument_list|()
expr_stmt|;
block|}
DECL|method|transform (Exchange exchange)
specifier|public
name|String
name|transform
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|SessionID
name|sessionID
init|=
operator|(
name|SessionID
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|QuickfixjEndpoint
operator|.
name|SESSION_ID_KEY
argument_list|)
decl_stmt|;
name|Session
name|session
init|=
name|Session
operator|.
name|lookupSession
argument_list|(
name|sessionID
argument_list|)
decl_stmt|;
name|DataDictionary
name|dataDictionary
init|=
name|session
operator|.
name|getDataDictionary
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataDictionary
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No Data Dictionary. Exchange must reference an existing session"
argument_list|)
throw|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\"event\": {\n"
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"  \""
argument_list|)
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|"\": "
argument_list|)
operator|.
name|append
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|",\n"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|renderer
operator|.
name|transform
argument_list|(
name|in
operator|.
name|getBody
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
literal|"  "
argument_list|,
name|dataDictionary
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"}\n"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

