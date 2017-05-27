begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|List
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|Ordered
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|OrderedComparatorTest
specifier|public
class|class
name|OrderedComparatorTest
extends|extends
name|TestCase
block|{
DECL|method|testOrderedComparatorGet ()
specifier|public
name|void
name|testOrderedComparatorGet
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Ordered
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordered
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|sort
argument_list|(
name|OrderedComparator
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrderedComparator ()
specifier|public
name|void
name|testOrderedComparator
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Ordered
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordered
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|sort
argument_list|(
operator|new
name|OrderedComparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrderedComparatorGetReverse ()
specifier|public
name|void
name|testOrderedComparatorGetReverse
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Ordered
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordered
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|sort
argument_list|(
name|OrderedComparator
operator|.
name|getReverse
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrderedComparatorReverse ()
specifier|public
name|void
name|testOrderedComparatorReverse
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Ordered
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordered
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|sort
argument_list|(
operator|new
name|OrderedComparator
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrderedComparatorHigh ()
specifier|public
name|void
name|testOrderedComparatorHigh
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Ordered
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordered
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
name|Ordered
operator|.
name|HIGHEST
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|sort
argument_list|(
operator|new
name|OrderedComparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Ordered
operator|.
name|HIGHEST
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrderedComparatorHighReverse ()
specifier|public
name|void
name|testOrderedComparatorHighReverse
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Ordered
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordered
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
name|Ordered
operator|.
name|HIGHEST
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|sort
argument_list|(
operator|new
name|OrderedComparator
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Ordered
operator|.
name|HIGHEST
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrderedComparatorLow ()
specifier|public
name|void
name|testOrderedComparatorLow
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Ordered
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordered
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
operator|-
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
name|Ordered
operator|.
name|LOWEST
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
operator|-
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|sort
argument_list|(
operator|new
name|OrderedComparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|4
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|2
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Ordered
operator|.
name|LOWEST
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrderedComparatorLowReverse ()
specifier|public
name|void
name|testOrderedComparatorLowReverse
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Ordered
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Ordered
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
operator|-
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
name|Ordered
operator|.
name|LOWEST
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
operator|new
name|MyOrder
argument_list|(
operator|-
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|sort
argument_list|(
operator|new
name|OrderedComparator
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Ordered
operator|.
name|LOWEST
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|2
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|4
argument_list|,
name|answer
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyOrder
specifier|private
specifier|static
specifier|final
class|class
name|MyOrder
implements|implements
name|Ordered
block|{
DECL|field|order
specifier|private
specifier|final
name|int
name|order
decl_stmt|;
DECL|method|MyOrder (int order)
specifier|private
name|MyOrder
parameter_list|(
name|int
name|order
parameter_list|)
block|{
name|this
operator|.
name|order
operator|=
name|order
expr_stmt|;
block|}
DECL|method|getOrder ()
specifier|public
name|int
name|getOrder
parameter_list|()
block|{
return|return
name|order
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|""
operator|+
name|order
return|;
block|}
block|}
block|}
end_class

end_unit

