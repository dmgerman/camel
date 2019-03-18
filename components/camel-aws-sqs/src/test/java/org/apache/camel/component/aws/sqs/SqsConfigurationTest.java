begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sqs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sqs
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
DECL|class|SqsConfigurationTest
specifier|public
class|class
name|SqsConfigurationTest
block|{
annotation|@
name|Test
DECL|method|itReturnsAnInformativeErrorForBadMessageGroupIdStrategy ()
specifier|public
name|void
name|itReturnsAnInformativeErrorForBadMessageGroupIdStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|SqsConfiguration
name|sqsConfiguration
init|=
operator|new
name|SqsConfiguration
argument_list|()
decl_stmt|;
try|try
block|{
name|sqsConfiguration
operator|.
name|setMessageGroupIdStrategy
argument_list|(
literal|"useUnknownStrategy"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Bad error message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Unrecognised MessageGroupIdStrategy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|itReturnsAnInformativeErrorForBadMessageDeduplicationIdStrategy ()
specifier|public
name|void
name|itReturnsAnInformativeErrorForBadMessageDeduplicationIdStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|SqsConfiguration
name|sqsConfiguration
init|=
operator|new
name|SqsConfiguration
argument_list|()
decl_stmt|;
try|try
block|{
name|sqsConfiguration
operator|.
name|setMessageDeduplicationIdStrategy
argument_list|(
literal|"useUnknownStrategy"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Bad error message: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Unrecognised MessageDeduplicationIdStrategy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

