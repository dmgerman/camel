begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ddbstream
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
name|ddbstream
package|;
end_package

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
name|Collection
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
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
name|assertThat
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|ShardListAtSequenceParametrised
specifier|public
class|class
name|ShardListAtSequenceParametrised
block|{
DECL|field|undertest
specifier|private
name|ShardList
name|undertest
decl_stmt|;
DECL|field|inputSequenceNumber
specifier|private
specifier|final
name|String
name|inputSequenceNumber
decl_stmt|;
DECL|field|expectedShardId
specifier|private
specifier|final
name|String
name|expectedShardId
decl_stmt|;
DECL|method|ShardListAtSequenceParametrised (String inputSequenceNumber, String expectedShardId)
specifier|public
name|ShardListAtSequenceParametrised
parameter_list|(
name|String
name|inputSequenceNumber
parameter_list|,
name|String
name|expectedShardId
parameter_list|)
block|{
name|this
operator|.
name|inputSequenceNumber
operator|=
name|inputSequenceNumber
expr_stmt|;
name|this
operator|.
name|expectedShardId
operator|=
name|expectedShardId
expr_stmt|;
block|}
annotation|@
name|Parameterized
operator|.
name|Parameters
DECL|method|paramaters ()
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|paramaters
parameter_list|()
block|{
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"0"
block|,
literal|"a"
block|}
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"3"
block|,
literal|"a"
block|}
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"6"
block|,
literal|"b"
block|}
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"8"
block|,
literal|"b"
block|}
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"15"
block|,
literal|"b"
block|}
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"16"
block|,
literal|"c"
block|}
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"18"
block|,
literal|"d"
block|}
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"25"
block|,
literal|"d"
block|}
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"30"
block|,
literal|"d"
block|}
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|undertest
operator|=
operator|new
name|ShardList
argument_list|()
expr_stmt|;
name|undertest
operator|.
name|addAll
argument_list|(
name|ShardListTest
operator|.
name|createShardsWithSequenceNumbers
argument_list|(
literal|null
argument_list|,
literal|"a"
argument_list|,
literal|"1"
argument_list|,
literal|"5"
argument_list|,
literal|"b"
argument_list|,
literal|"8"
argument_list|,
literal|"15"
argument_list|,
literal|"c"
argument_list|,
literal|"16"
argument_list|,
literal|"16"
argument_list|,
literal|"d"
argument_list|,
literal|"20"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|assertions ()
specifier|public
name|void
name|assertions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|undertest
operator|.
name|atSeq
argument_list|(
name|inputSequenceNumber
argument_list|)
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
name|expectedShardId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
