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
name|quickfix
operator|.
name|ConfigError
import|;
end_import

begin_class
DECL|class|QuickfixjMessageJsonPrinter
specifier|public
class|class
name|QuickfixjMessageJsonPrinter
block|{
DECL|field|formatter
specifier|private
name|QuickfixjEventJsonTransformer
name|formatter
decl_stmt|;
DECL|method|QuickfixjMessageJsonPrinter ()
specifier|public
name|QuickfixjMessageJsonPrinter
parameter_list|()
throws|throws
name|ConfigError
block|{
name|formatter
operator|=
operator|new
name|QuickfixjEventJsonTransformer
argument_list|()
expr_stmt|;
block|}
DECL|method|print (Exchange exchange)
specifier|public
name|void
name|print
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|formatter
operator|.
name|transform
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

