begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.resequencer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|resequencer
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|SequenceTest
specifier|public
class|class
name|SequenceTest
extends|extends
name|Assert
block|{
DECL|field|e1
specifier|private
name|TestObject
name|e1
decl_stmt|;
DECL|field|e2
specifier|private
name|TestObject
name|e2
decl_stmt|;
DECL|field|e3
specifier|private
name|TestObject
name|e3
decl_stmt|;
DECL|field|set
specifier|private
name|Sequence
argument_list|<
name|TestObject
argument_list|>
name|set
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|e1
operator|=
operator|new
name|TestObject
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|e2
operator|=
operator|new
name|TestObject
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|e3
operator|=
operator|new
name|TestObject
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|set
operator|=
operator|new
name|Sequence
argument_list|<>
argument_list|(
operator|new
name|TestComparator
argument_list|()
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|e3
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|e1
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
name|e2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Test
DECL|method|testPredecessor ()
specifier|public
name|void
name|testPredecessor
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|e1
argument_list|,
name|set
operator|.
name|predecessor
argument_list|(
name|e2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|set
operator|.
name|predecessor
argument_list|(
name|e1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|set
operator|.
name|predecessor
argument_list|(
name|e3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSuccessor ()
specifier|public
name|void
name|testSuccessor
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|e2
argument_list|,
name|set
operator|.
name|successor
argument_list|(
name|e1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|set
operator|.
name|successor
argument_list|(
name|e2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|set
operator|.
name|successor
argument_list|(
name|e3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

