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

begin_expr_stmt
operator|.
name|internal
expr_stmt|;
end_expr_stmt

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
operator|.
name|ApiMethodPropertiesHelper
import|;
end_import

begin_import
import|import
name|$
block|{
package|package
block|}
end_import

begin_expr_stmt
operator|.
name|$
block|{
name|name
block|}
name|Configuration
expr_stmt|;
end_expr_stmt

begin_comment
comment|/**  * Singleton {@link ApiMethodPropertiesHelper} for ${name} component.  */
end_comment

begin_class
DECL|class|$
specifier|public
specifier|final
class|class
name|$
block|{
name|name
block|}
end_class

begin_expr_stmt
DECL|class|$
name|PropertiesHelper
expr|extends
name|ApiMethodPropertiesHelper
operator|<
name|$
block|{
name|name
block|}
name|Configuration
operator|>
block|{
specifier|private
specifier|static
name|$
block|{
name|name
block|}
name|PropertiesHelper
name|helper
block|;
specifier|private
name|$
block|{
name|name
block|}
name|PropertiesHelper
argument_list|()
block|{
name|super
argument_list|(
name|$
block|{
name|name
block|}
name|Configuration
operator|.
name|class
argument_list|,
name|$
block|{
name|name
block|}
name|Constants
operator|.
name|PROPERTY_PREFIX
argument_list|)
block|;     }
specifier|public
specifier|static
specifier|synchronized
name|$
block|{
name|name
block|}
name|PropertiesHelper
name|getHelper
argument_list|()
block|{
if|if
condition|(
name|helper
operator|==
literal|null
condition|)
block|{
name|helper
operator|=
operator|new
name|$
block|{
name|name
block|}
name|PropertiesHelper
argument_list|()
expr_stmt|;
block|}
return|return
name|helper
return|;
block|}
end_expr_stmt

unit|}
end_unit

