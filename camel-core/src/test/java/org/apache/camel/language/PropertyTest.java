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
name|LanguageTestSupport
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

begin_class
DECL|class|PropertyTest
specifier|public
class|class
name|PropertyTest
extends|extends
name|LanguageTestSupport
block|{
DECL|method|testProertyExpressions ()
specifier|public
name|void
name|testProertyExpressions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertExpression
argument_list|(
literal|"quote"
argument_list|,
literal|"Camel rocks"
argument_list|)
expr_stmt|;
block|}
DECL|method|testPredicates ()
specifier|public
name|void
name|testPredicates
parameter_list|()
throws|throws
name|Exception
block|{
name|assertPredicate
argument_list|(
literal|"quote"
argument_list|)
expr_stmt|;
block|}
DECL|method|getLanguageName ()
specifier|protected
name|String
name|getLanguageName
parameter_list|()
block|{
return|return
literal|"property"
return|;
block|}
annotation|@
name|Override
DECL|method|populateExchange (Exchange exchange)
specifier|protected
name|void
name|populateExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
operator|.
name|populateExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"quote"
argument_list|,
literal|"Camel rocks"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

