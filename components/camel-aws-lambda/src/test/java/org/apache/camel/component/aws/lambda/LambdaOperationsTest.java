begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.lambda
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
name|lambda
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
name|assertEquals
import|;
end_import

begin_class
DECL|class|LambdaOperationsTest
specifier|public
class|class
name|LambdaOperationsTest
block|{
annotation|@
name|Test
DECL|method|supportedOperationCount ()
specifier|public
name|void
name|supportedOperationCount
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|14
argument_list|,
name|LambdaOperations
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|valueOf ()
specifier|public
name|void
name|valueOf
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|createFunction
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"createFunction"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|getFunction
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"getFunction"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|listFunctions
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"listFunctions"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|invokeFunction
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"invokeFunction"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|deleteFunction
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"deleteFunction"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|updateFunction
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"updateFunction"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|createEventSourceMapping
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"createEventSourceMapping"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|deleteEventSourceMapping
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"deleteEventSourceMapping"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|listEventSourceMapping
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"listEventSourceMapping"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|listTags
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"listTags"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|tagResource
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"tagResource"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|untagResource
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"untagResource"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|publishVersion
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"publishVersion"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|listVersions
argument_list|,
name|LambdaOperations
operator|.
name|valueOf
argument_list|(
literal|"listVersions"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|createFunction
operator|.
name|toString
argument_list|()
argument_list|,
literal|"createFunction"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|getFunction
operator|.
name|toString
argument_list|()
argument_list|,
literal|"getFunction"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|listFunctions
operator|.
name|toString
argument_list|()
argument_list|,
literal|"listFunctions"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|invokeFunction
operator|.
name|toString
argument_list|()
argument_list|,
literal|"invokeFunction"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|deleteFunction
operator|.
name|toString
argument_list|()
argument_list|,
literal|"deleteFunction"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|updateFunction
operator|.
name|toString
argument_list|()
argument_list|,
literal|"updateFunction"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|createEventSourceMapping
operator|.
name|toString
argument_list|()
argument_list|,
literal|"createEventSourceMapping"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|deleteEventSourceMapping
operator|.
name|toString
argument_list|()
argument_list|,
literal|"deleteEventSourceMapping"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|listEventSourceMapping
operator|.
name|toString
argument_list|()
argument_list|,
literal|"listEventSourceMapping"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|listTags
operator|.
name|toString
argument_list|()
argument_list|,
literal|"listTags"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|tagResource
operator|.
name|toString
argument_list|()
argument_list|,
literal|"tagResource"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|untagResource
operator|.
name|toString
argument_list|()
argument_list|,
literal|"untagResource"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|publishVersion
operator|.
name|toString
argument_list|()
argument_list|,
literal|"publishVersion"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LambdaOperations
operator|.
name|listVersions
operator|.
name|toString
argument_list|()
argument_list|,
literal|"listVersions"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

