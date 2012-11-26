begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
package|;
end_package

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
comment|/**  * consumes tweets  */
end_comment

begin_class
DECL|class|DailyTrendDirectTest
specifier|public
class|class
name|DailyTrendDirectTest
extends|extends
name|CamelTwitterConsumerTestSupport
block|{
annotation|@
name|Override
DECL|method|getUri ()
specifier|protected
name|String
name|getUri
parameter_list|()
block|{
comment|// DO NOT test with 'date='.  Twitter only allows dates up to
comment|// a certain limit, so we can't have that as a long-term test
return|return
literal|"twitter://trends/daily?type=direct&"
return|;
block|}
annotation|@
name|Override
DECL|method|getLogger ()
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DailyTrendDirectTest
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

