begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_expr_stmt
unit|#
operator|#
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
operator|--
expr|#
operator|#
name|Licensed
name|to
name|the
name|Apache
name|Software
name|Foundation
argument_list|(
name|ASF
argument_list|)
name|under
name|one
name|or
name|more
expr|#
operator|#
name|contributor
name|license
name|agreements
operator|.
name|See
name|the
name|NOTICE
name|file
name|distributed
name|with
expr|#
operator|#
name|this
name|work
end_expr_stmt

begin_for
for|for additional information regarding copyright ownership. ## The ASF licenses this file to You under the Apache License
operator|,
name|Version
literal|2.0
expr|#
operator|#
operator|(
name|the
literal|"License"
operator|)
expr_stmt|;
end_for

begin_expr_stmt
name|you
name|may
name|not
name|use
name|this
name|file
name|except
name|in
name|compliance
name|with
expr|#
operator|#
name|the
name|License
operator|.
name|You
name|may
name|obtain
name|a
name|copy
name|of
name|the
name|License
name|at
expr|#
operator|#
expr|#
operator|#
name|http
operator|:
comment|//www.apache.org/licenses/LICENSE-2.0
expr|#
operator|#
expr|#
operator|#
name|Unless
name|required
name|by
name|applicable
name|law
name|or
name|agreed
name|to
name|in
name|writing
operator|,
name|software
expr|#
operator|#
name|distributed
name|under
name|the
name|License
name|is
name|distributed
name|on
name|an
literal|"AS IS"
name|BASIS
operator|,
expr_stmt|#
operator|#
name|WITHOUT
name|WARRANTIES
name|OR
name|CONDITIONS
name|OF
name|ANY
name|KIND
operator|,
name|either
name|express
name|or
name|implied
operator|.
expr|#
operator|#
name|See
name|the
name|License
end_expr_stmt

begin_for
for|for the specific language governing permissions and ## limitations under the License. ## ------------------------------------------------------------------------ package $
block|{
package|package
block|}
end_for

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|PropertyInject
import|;
end_import

begin_comment
comment|/**  * Class to configure the Camel application.  */
end_comment

begin_class
DECL|class|MyConfiguration
specifier|public
class|class
name|MyConfiguration
block|{
annotation|@
name|BindToRegistry
DECL|method|myBean (@ropertyInjectR) String hi, @PropertyInject(R) String bye)
specifier|public
name|MyBean
name|myBean
parameter_list|(
annotation|@
name|PropertyInject
argument_list|(
literal|"hi"
argument_list|)
name|String
name|hi
parameter_list|,
annotation|@
name|PropertyInject
argument_list|(
literal|"bye"
argument_list|)
name|String
name|bye
parameter_list|)
block|{
comment|// this will create an instance of this bean with the name of the method (eg myBean)
return|return
operator|new
name|MyBean
argument_list|(
name|hi
argument_list|,
name|bye
argument_list|)
return|;
block|}
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// this method is optional and can be removed if no additional configuration is needed.
block|}
block|}
end_class

end_unit

